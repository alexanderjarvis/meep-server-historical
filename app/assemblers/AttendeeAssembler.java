package assemblers;

import java.util.ArrayList;
import java.util.List;

import models.Attendee;
import models.Meeting;
import models.User;
import models.Attendee.MeetingResponse;
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
		attendeeDTO.id = attendee.user.id;
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
	
	/**
	 * 
	 * @param attendeeDTOs
	 * @param meeting
	 */
	public static void createAttendees(List<AttendeeDTO> attendeeDTOs, Meeting meeting) {
		
		// Make the meeting owner the first attendee and set the rsvp to accept.
		AttendeeDTO ownerAttendeeDTO = new AttendeeDTO();
		ownerAttendeeDTO.id = meeting.owner.id;
		createAttendee(ownerAttendeeDTO, meeting);
		MeetingHelper.acceptMeetingRequest(meeting, meeting.owner);
		
		// Create the other attendees
		for (AttendeeDTO attendeeDTO : attendeeDTOs) {
			if (!attendeeDTO.id.equals(ownerAttendeeDTO.id)) {
				createAttendee(attendeeDTO, meeting);
			}
		}
	}
	
	/**
	 * 
	 * @param attendeeDTO
	 * @param meeting
	 */
	public static void createAttendee(AttendeeDTO attendeeDTO, Meeting meeting) {
		User user = User.findById(attendeeDTO.id);
		if (user != null) {
			MeetingHelper.createAttendee(meeting, user);
		}
	}
	
	/**
	 * Adds or removes attendees to/from a meeting.
	 * 
	 * Note: Does not update attendees rsvp status as this is done by the user themselves.
	 * 
	 * @param attendeeDTOs
	 * @return
	 */
	public static void updateAttendees(Meeting meeting, List<AttendeeDTO> attendeeDTOs) {
		
		// If the Attendee is in the persisted list, but not the DTO list, then remove it.
		List<Attendee> attendeesToDelete = new ArrayList<Attendee>();
		for (Attendee attendee : meeting.attendees) {
			if (!isAttendeeInDTOList(attendee, attendeeDTOs)) {
				attendeesToDelete.add(attendee);
			}
		}
		// Delete the attendees
		// They cannot be deleted inside the previous loop as this operation modifies the collection.
		for (Attendee attendee : attendeesToDelete) {
			attendee.delete();
		}
		
		// If the attendee in the DTO list is not in the persisted list, then add it.
		for (AttendeeDTO attendeeDTO : attendeeDTOs) {
			
			if (!isAttendeeDTOInList(attendeeDTO, meeting.attendees)) {
				createAttendee(attendeeDTO, meeting);
			}
		}
		
	}
	
	private static boolean isAttendeeInDTOList(Attendee attendee, List<AttendeeDTO> attendeeDTOList) {
		for (AttendeeDTO attendeeDTO : attendeeDTOList) {
			if (attendee.user.id.equals(attendeeDTO.id)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isAttendeeDTOInList(AttendeeDTO attendeeDTO, List<Attendee> attendeeList) {
		for (Attendee attendee : attendeeList) {
			if (attendee.user.id.equals(attendeeDTO.id)) {
				return true;
			}
		}
		return false;
	}

}
