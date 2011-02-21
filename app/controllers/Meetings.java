package controllers;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import models.Meeting;
import models.User;
import play.mvc.With;
import DTO.MeetingDTO;
import assemblers.MeetingAssembler;
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
    	MeetingDTO meetingDTO = new Gson().fromJson(body, MeetingDTO.class);
    	MeetingDTO newMeetingDTO = MeetingAssembler.createMeeting(meetingDTO);
    	response.status = 201;
		renderJSON(newMeetingDTO);
    }
    
    public static void show(Long id) {
    	//TODO: auth rules - i.e. only users connected to the meeting can view it
    	Meeting meeting = Meeting.findById(id);
    	if (meeting != null) {
    		renderJSON(MeetingAssembler.writeDTO(meeting));
    	} else {
    		error(404, "Not found");
    	}
    }

}
