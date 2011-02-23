package assemblers;

import java.util.ArrayList;
import java.util.List;

import models.Attendee;
import models.Meeting;
import models.User;
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
		
		for (AttendeeDTO attendeeDTO : attendeeDTOs) {
			createAttendee(attendeeDTO, meeting);
		}
	}
	
	public static Attendee createAttendee(AttendeeDTO attendeeDTO, Meeting meeting) {
		Attendee attendee = new Attendee();
		attendee.meeting = meeting;
		meeting.attendees.add(attendee);
		attendee.user = User.findById(attendeeDTO.id);
		attendee.user.meetingsRelated.add(attendee);
		attendee.save();
		return attendee;
		
	}

}
