package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import models.Meeting;
import models.User;
import play.Logger;
import play.mvc.With;
import DTO.MeetingDTO;
import assemblers.MeetingAssembler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
    	///////// Custom code until GsonBinder works with FunctionalTest
    	StringBuilder sb = new StringBuilder();
    	BufferedReader buf = new BufferedReader(new InputStreamReader(request.body));
    	String line;
    	try {
			while((line = buf.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	Logger.debug("body: " + sb.toString());
    	Logger.debug("querystring: " + request.querystring);
    	
    	JsonObject bodyObject =  (JsonObject) new JsonParser().parse(sb.toString());
    	///////// Custom code until GsonBinder works with FunctionalTest
    	
    	if (bodyObject.isJsonObject()) {
    	
	    	MeetingDTO meetingDTO = new Gson().fromJson(bodyObject, MeetingDTO.class);
	    	if (meetingDTO != null) {
		    	MeetingDTO newMeetingDTO = MeetingAssembler.createMeeting(meetingDTO);
		    	response.status = 201;
				renderJSON(newMeetingDTO);
	    	}
	    }
    	badRequest();
    }
    
    public static void show(Long id) {
    	//TODO: auth rules - i.e. only users connected to the meeting can view it
    	Meeting meeting = Meeting.findById(id);
    	if (meeting != null) {
    		renderJSON(MeetingAssembler.writeDTO(meeting));
    	} else {
    		notFound();
    	}
    }

}
