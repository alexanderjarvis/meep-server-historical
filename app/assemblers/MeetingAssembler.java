package assemblers;

import java.util.ArrayList;
import java.util.List;

import models.Attendee;
import models.Meeting;
import models.User;
import DTO.MeetingDTO;

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
		//meetingDTO.attendees = AttendeeAssembler.writeDTOs(meeting.attendees);
		//meetingDTO.owner = UserSummaryAssembler.writeDTO(meeting.owner);
		meetingDTO.title = meeting.title;
		meetingDTO.description = meeting.description;
		meetingDTO.type = meeting.type;
		
		return meetingDTO;
	}
	
	public static List<MeetingDTO> writeDTOs(User user) {
		ArrayList<MeetingDTO> meetings = new ArrayList<MeetingDTO>();
		for (Attendee attendee : user.meetingsRelated) {
			meetings.add(writeDTO(attendee.meeting));
		}
		return meetings;
	}
	
	public static MeetingDTO createMeeting(MeetingDTO meetingDTO) {
		Meeting meeting = new Meeting();
		
		meeting.time = meetingDTO.time;
		if (meetingDTO.place != null) {
			meeting.place = CoordinateAssembler.createCoordinate(meetingDTO.place);
		}
		if (meetingDTO.owner != null) {
			meeting.owner = User.findById(meetingDTO.owner.id);
		}
		meeting.title = meetingDTO.title;
		meeting.description = meetingDTO.description;
		meeting.type = meetingDTO.type;
		
		// The Attendee is the owning side of the relationship, so the meeting must be
		// saved first.
		meeting.save();
		if (meetingDTO.attendees != null) {
			AttendeeAssembler.createAttendees(meetingDTO.attendees, meeting);
		}
		
		return writeDTO(meeting);
	}

}
