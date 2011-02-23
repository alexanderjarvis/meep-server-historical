package controllers;

import java.util.List;

import models.Attendee;
import models.Meeting;
import models.User;
import play.mvc.With;
import results.RenderCustomJson;
import DTO.MeetingDTO;
import assemblers.MeetingAssembler;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import controllers.oauth2.AccessTokenFilter;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
@With(JSONRequestTypeFilter.class)
public class Meetings extends AccessTokenFilter {

    public static void index() {
    	User authUser = userAuth.getAuthorisedUser();
    	List<MeetingDTO> meetings = MeetingAssembler.writeDTOs(authUser);
        renderJSON(meetings);
    }
    
    public static void create(JsonObject body) {
    	
    	if (body != null && body.isJsonObject()) {
    		GsonBuilder gsonBuilder = new GsonBuilder();
    		gsonBuilder.setDateFormat(RenderCustomJson.ISO8601_DATE_FORMAT);
    		
	    	MeetingDTO meetingDTO = gsonBuilder.create().fromJson(body, MeetingDTO.class);
	    	if (meetingDTO != null) {
		    	MeetingDTO newMeetingDTO = MeetingAssembler.createMeeting(meetingDTO, userAuth.getAuthorisedUser());
		    	response.status = 201;
				renderJSON(newMeetingDTO);
	    	}
	    }
    	badRequest();
    }
    
    public static void show(Long id) {
    	
    	User authUser = userAuth.getAuthorisedUser();
    	for (Attendee attendee : authUser.meetingsRelated) {
    		if (id.equals(attendee.meeting.id)) {
    			renderJSON(MeetingAssembler.writeDTO(attendee.meeting));
    		}
    	}
    	
    	notFound();
    }

}
