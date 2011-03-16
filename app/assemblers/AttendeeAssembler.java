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
 * Assembler for the AttendeeDTO and Attendee classes.
 * 
 * @see AttendeeDTO
 * @see Attendee
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class AttendeeAssembler {
	
	/**
	 * Writes an AttendeeDTO from an Attendee object.
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
	 * Writes a List of AttendeeDTOs from a List of Attendees
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
	 * Creates the Attendees for a Meeting using a List of AttendeDTOs
	 * 
	 * Also makes the owner of the meeting an attendee and sets their rsvp status.
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
	 * Creates an Attendee for a meeting with an AttendeeDTO
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
	 * Note: Does not update attendees RSVP status as this is done by the user themselves.
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
	
	/**
	 * Returns true if the Attendee is in the AttendeeDTO List and false otherwise.
	 * @param attendee
	 * @param attendeeDTOList
	 * @return
	 */
	private static boolean isAttendeeInDTOList(Attendee attendee, List<AttendeeDTO> attendeeDTOList) {
		for (AttendeeDTO attendeeDTO : attendeeDTOList) {
			if (attendee.user.id.equals(attendeeDTO.id)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if the AttendeeDTO is in the Attendee List and false otherwise.
	 * @param attendeeDTO
	 * @param attendeeList
	 * @return
	 */
	private static boolean isAttendeeDTOInList(AttendeeDTO attendeeDTO, List<Attendee> attendeeList) {
		for (Attendee attendee : attendeeList) {
			if (attendee.user.id.equals(attendeeDTO.id)) {
				return true;
			}
		}
		return false;
	}

}
