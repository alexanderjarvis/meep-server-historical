package assemblers;

import java.util.ArrayList;
import java.util.List;

import models.Attendee;
import models.Meeting;
import models.User;
import results.RenderCustomJson;
import DTO.MeetingDTO;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.*;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class MeetingAssembler {
	
	public static MeetingDTO writeDTO(Meeting meeting) {
		MeetingDTO meetingDTO = new MeetingDTO();
		meetingDTO.id = meeting.id;
		meetingDTO.time = meeting.time;
		meetingDTO.place = CoordinateAssembler.writeDTO(meeting.place);
		meetingDTO.attendees = AttendeeAssembler.writeDTOs(meeting.attendees);
		meetingDTO.owner = UserSummaryAssembler.writeDTO(meeting.owner);
		meetingDTO.title = meeting.title;
		meetingDTO.description = meeting.description;
		
		return meetingDTO;
	}
	
	public static List<MeetingDTO> writeDTOs(User user) {
		ArrayList<MeetingDTO> meetings = new ArrayList<MeetingDTO>();
		for (Attendee attendee : user.meetingsRelated) {
			meetings.add(writeDTO(attendee.meeting));
		}
		return meetings;
	}
	
	public static MeetingDTO createMeeting(MeetingDTO meetingDTO, User user) {
		Meeting meeting = new Meeting();
		meeting.time = meetingDTO.time;
		if (meetingDTO.place != null) {
			meeting.place = CoordinateAssembler.createCoordinate(meetingDTO.place);
		}
		meeting.owner = user;
		meeting.title = meetingDTO.title;
		meeting.description = meetingDTO.description;
		
		// The Attendee is the owning side of the relationship, so the meeting must be saved first.
		meeting.save();
		if (meetingDTO.attendees != null) {
			AttendeeAssembler.createAttendees(meetingDTO.attendees, meeting);
		}
		
		return writeDTO(meeting);
	}
	
	public static MeetingDTO updateMeeting(MeetingDTO meetingDTO) {
		Meeting meeting = Meeting.findById(meetingDTO.id);
		if (meeting != null) {
			meeting.time = meetingDTO.time;
			
			// Set the id of the place to the DTO because we don't expect the client to send this
			// information as the place belongs to the meeting anyway and the id is just used to lookup
			// the Coordinate. An alternative would be to pass the meeting.place object into
			// CoordinateAssembler.updateCoordinate() but this would break convention.
			meetingDTO.place.id = meeting.place.id;
			CoordinateAssembler.updateCoordinate(meetingDTO.place);
			meeting.title = meetingDTO.title;
			meeting.description = meetingDTO.description;
			AttendeeAssembler.updateAttendees(meeting, meetingDTO.attendees);
			
			return writeDTO(meeting);
		}
		return null;
	}
	
	/**
	 * Updates the meeting with a JsonObject
	 * @param jsonObject
	 * @return
	 */
	public static MeetingDTO updateMeetingWithJsonObject(JsonObject jsonObject) {
		MeetingDTO meetingDTO = meetingDTOWithJsonObject(jsonObject);
		meetingDTO = updateMeeting(meetingDTO);
		return meetingDTO;
	}
	
	/**
	 * Returns a MeetingDTO object from a JsonObject, using Gson.
	 * @param jsonObject
	 * @return
	 */
	public static MeetingDTO meetingDTOWithJsonObject(JsonObject jsonObject) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setDateFormat(RenderCustomJson.ISO8601_DATE_FORMAT);
		MeetingDTO meetingDTO = gsonBuilder.create().fromJson(jsonObject, MeetingDTO.class);
		return meetingDTO;
	}
	
	/**
	 * Returns a MeetingDTO object from a Json string, using Gson.
	 * @param jsonString
	 * @return
	 */
	public static MeetingDTO meetingDTOWithJsonString(String jsonString) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setDateFormat(RenderCustomJson.ISO8601_DATE_FORMAT);
		MeetingDTO meetingDTO = gsonBuilder.create().fromJson(jsonString, MeetingDTO.class);
		return meetingDTO;
	}

}
