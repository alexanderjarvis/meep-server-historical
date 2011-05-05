package controllers;

import java.util.List;

import notifiers.Mails;

import models.Attendee;
import models.Meeting;
import models.User;
import models.helpers.MeetingHelper;
import play.Logger;
import play.data.validation.Error;
import DTO.MeetingDTO;
import assemblers.MeetingAssembler;

import com.google.gson.JsonObject;

/**
 * The Meetings controller is used to control all aspects of a meeting.
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class Meetings extends ServiceApplicationController {
	
	/**
	 * 
	 */
    public static void index() {
    	User authUser = getAuthorisedUser();
    	List<MeetingDTO> meetings = MeetingAssembler.writeDTOs(authUser);
        renderJSON(meetings);
    }
    
    /**
     * 
     * @param body
     */
    public static void create(JsonObject body) {
    	
    	if (body != null && body.isJsonObject()) {
    		
	    	MeetingDTO meetingDTO = MeetingAssembler.meetingDTOWithJsonObject(body);
	    	
	    	if (meetingDTO != null) {
	    		
	    		validation.valid(meetingDTO);
	    		if (validation.hasErrors()) {
	        		for (Error error : validation.errors()) {
	        			Logger.debug(error.getKey() + " : " + error.message());
	        		}
	    			error(400, "Validation Errors");
	    		}
	    		
		    	MeetingDTO newMeetingDTO = MeetingAssembler.createMeeting(meetingDTO, getAuthorisedUser());
		    	
		    	// Send email to each attendee
		    	Mails.newMeeting(newMeetingDTO);
		    	
		    	response.status = 201;
				renderJSON(newMeetingDTO);
	    	}
	    }
    	badRequest();
    }
    
    /**
     * 
     * @param id
     */
    public static void show(Long id) {
    	
    	Meeting meeting = Meeting.findById(id);
    	if (meeting != null) {
	    	// Check the authorised user is able to view the meeting
	    	User authUser = getAuthorisedUser();
	    	for (Attendee attendee : meeting.attendees) {
	    		if (attendee.user.id.equals(authUser.id)) {
	    			renderJSON(MeetingAssembler.writeDTO(meeting));
	    		}
	    	}
    	}
    	
    	notFound();
    }
    
    /**
     * 
     * @param body
     * @param id
     */
    public static void update(Long id, JsonObject body) {
    	
    	if (body != null && body.isJsonObject()) {
    		Meeting meeting = Meeting.findById(id);
    		
    		// Check meeting exists and that the id in the JSON matches the id in the URL
    		if (meeting != null && body.get("id") != null && new Long(body.get("id").getAsLong()).equals(id)) {
    			
    			// Check the authorised user is the owner of the meeting
    			if (meeting.owner.equals(getAuthorisedUser())) {
    				MeetingDTO newMeetingDTO = MeetingAssembler.updateMeetingWithJsonObject(body);
    	    		renderJSON(newMeetingDTO);
    			} else {
    				badRequest();
    			}
    		   
    		} else {
    			notFound();
    		}
	    }
    	badRequest();
    }
    
    /**
     * 
     * @param id
     */
    public static void delete(Long id) {
    	
    	Meeting meeting = Meeting.findById(id);
		
		if (meeting != null) {
			// Check meeting exists and that the Authorised user is the owner of the meeting.
			if (meeting.owner.equals(getAuthorisedUser())) {
				meeting.delete();
				ok();
			} else {
				badRequest();
			}
		} else {
			notFound();
		}
    }
    
    /**
     * 
     * @param id
     */
    public static void acceptMeetingRequest(Long id) {
    	Meeting meeting = Meeting.findById(id);
    	if (meeting != null) {
    		User authUser = getAuthorisedUser();
    		if (MeetingHelper.acceptMeetingRequest(meeting, authUser)) {
    			ok();
    		} else {
    			badRequest();
    		}
    	}
    	notFound();
    }
    
    /**
     * 
     * @param id
     */
    public static void declineMeetingRequest(Long id) {
    	Meeting meeting = Meeting.findById(id);
    	if (meeting != null) {
    		User authUser = getAuthorisedUser();
    		if (MeetingHelper.declineMeetingRequest(meeting, authUser)) {
    			ok();
    		} else {
    			badRequest();
    		}
    	}
    	notFound();
    }
    
    /**
     * 
     * @param id
     * @param minutes
     */
    public static void updateMinutesBefore(Long id, String body) {
    	Integer minutes = null;
    	try {
    		minutes = Integer.parseInt(body);
    		if (minutes < 0) {
    			error("Minutes before must be a positive value");
    		}
    	} catch (NumberFormatException e) {
    		Logger.error("NumberFormatException", e);
    		error();
    	}
    	
    	if (minutes != null) {
	    	Meeting meeting = Meeting.findById(id);
	    	if (meeting != null) {
	    		User authUser = getAuthorisedUser();
	    		if (MeetingHelper.updateAttendeesMinutesBefore(minutes, meeting, authUser)) {
	    			ok();
	    		} else {
	    			badRequest();
	    		}
	    	}
    	}
    	notFound();
    }

}
