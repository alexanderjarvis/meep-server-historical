package notifiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import models.User;

import play.Play;
import play.exceptions.UnexpectedException;
import play.mvc.Mailer;
import DTO.AttendeeDTO;
import DTO.MeetingDTO;
import DTO.UserDTO;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class Mails extends Mailer {

	public static void welcome(UserDTO user) {
		setSubject("Welcome to Meep!");
		addRecipient(user.email);
		setFrom(Play.configuration.getProperty("mails.fromaddress"));
		send(user);
	}
	
	public static void newFriendRequest(UserDTO userTo, UserDTO userFrom) {
		setSubject("New Friend Request!");
		addRecipient(userTo.email);
		setFrom(Play.configuration.getProperty("mails.fromaddress"));
		send(userTo, userFrom);
	}
	
	public static void newMeeting(MeetingDTO meeting) {
		setSubject("New Meeting Request!");
		setFrom(Play.configuration.getProperty("mails.fromaddress"));
		
		// Send an email to each attendee.
		for (AttendeeDTO attendee : meeting.attendees) {
			if (!attendee.id.equals(meeting.owner.id)) {
				User user = User.findById(attendee.id);
				addRecipient(user.email);
				send(meeting, attendee);
			}
		}
		
	}
	
	// TODO: meeting deleted
	
	/**
	 * Sets a single recipient as opposed to the 'addRecipient' method.
	 * This allows multiple, customised emails to be sent within one Mailer.
	 */
	@SuppressWarnings("unchecked")
    public static void setRecipient(String recipient) {
        HashMap<String, Object> map = infos.get();
        if (map == null) {
            throw new UnexpectedException("Mailer not instrumented ?");
        }
        List recipientsList = (List<String>) map.get("recipients");
        if (recipientsList == null) {
            recipientsList = new ArrayList<String>();
            map.put("recipients", recipientsList);
        }
        recipientsList.addAll(Arrays.asList(new String[]{recipient}));
        infos.set(map);
    }

}
