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
		AttendeeDTO attendeeDTO = (AttendeeDTO) UserSummaryAssembler.writeDTO(attendee.user);
		attendeeDTO.rsvp = attendee.rsvp.toString();
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
		attendee.user = User.findById(attendeeDTO.id);
		attendee.save();
		return attendee;
		
	}

}
