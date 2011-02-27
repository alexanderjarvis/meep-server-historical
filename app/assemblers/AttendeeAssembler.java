package assemblers;

import java.util.ArrayList;
import java.util.List;

import models.Attendee;
import models.Meeting;
import models.User;
import models.helpers.MeetingHelper;
import DTO.AttendeeDTO;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class AttendeeAssembler {
	
	/**
	 * 
	 * @param attendee
	 * @return
	 */
	public static AttendeeDTO writeDTO(Attendee attendee) {
		AttendeeDTO attendeeDTO = new AttendeeDTO();
		attendeeDTO.id = attendee.id;
		attendeeDTO.firstName = attendee.user.firstName;
		attendeeDTO.lastName = attendee.user.lastName;
		if (attendee.rsvp != null) {
			attendeeDTO.rsvp = attendee.rsvp.toString();
		}
		return attendeeDTO;
	}
	
	/**
	 * 
	 * @param attendees
	 * @return
	 */
	public static List<AttendeeDTO> writeDTOs(List<Attendee> attendees) {
		ArrayList<AttendeeDTO> attendeeDTOs = new ArrayList<AttendeeDTO>();
		for (Attendee attendee : attendees) {
			attendeeDTOs.add(writeDTO(attendee));
		}
		return attendeeDTOs;
	}
	
	
	public static void createAttendees(List<AttendeeDTO> attendeeDTOs, Meeting meeting) {
		
		// Make the meeting owner the first attendee and set the rsvp to accept.
		AttendeeDTO ownerAttendeeDTO = new AttendeeDTO();
		ownerAttendeeDTO.id = meeting.owner.id;
		createAttendee(ownerAttendeeDTO, meeting);
		MeetingHelper.acceptMeetingRequest(meeting, meeting.owner);
		
		// Create the other attendees
		for (AttendeeDTO attendeeDTO : attendeeDTOs) {
			createAttendee(attendeeDTO, meeting);
		}
	}
	
	public static void createAttendee(AttendeeDTO attendeeDTO, Meeting meeting) {
		User user = User.findById(attendeeDTO.id);
		if (user != null) {
			MeetingHelper.createAttendee(meeting, user);
		}
	}

}
