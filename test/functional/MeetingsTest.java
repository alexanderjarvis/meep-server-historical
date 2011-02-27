package functional;

import models.Attendee;
import models.User;
import models.Attendee.MeetingResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import DTO.AttendeeDTO;
import DTO.MeetingDTO;

import assemblers.MeetingAssembler;

import com.google.gson.GsonBuilder;

import play.Logger;
import play.cache.Cache;
import play.mvc.Http;
import play.test.Fixtures;
import play.test.FunctionalTest;
import results.RenderCustomJson;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class MeetingsTest extends FunctionalTest {
	
	private Http.Request request;
	private Http.Response response;

	private static final String BASE_CONTROLLER_PATH = "/meetings";
	private String userBaseQuery = "?oauth_token=";
	private String user1Query = "";
	private String user2Query = "";
	private String user3Query = "";
	
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setUp() {
		Fixtures.deleteAll();
		Fixtures.load("data.yml");
		Cache.clear();
		
		request = new Http.Request();
		request.headers.put("accept", new Http.Header("Accept", "application/json"));
		
		user1 = User.find("byEmail", "bob@gmail.com").first();
		user1Query += userBaseQuery + user1.accessToken;
		
		user2 = User.find("byEmail", "bob2@gmail.com").first();
		user2Query += userBaseQuery + user2.accessToken;
		
		user3 = User.find("byEmail", "alex@jarvis.com").first();
		user3Query += userBaseQuery + user3.accessToken;
	}
	
	@After
	public void log() {
		if (response != null) {
			Logger.debug("Response Status: " + response.status.toString());
			Logger.debug("Response: \n" + response.out.toString());
		}
	}

	@Test
	public void testIndexPage() {
		response = GET(BASE_CONTROLLER_PATH + user1Query);
		assertIsOk(response);
		assertContentType("application/json", response);
	}

	@Test
	public void testCreate() {
		String body = "{\"time\":\"2011-06-01T12:00:00Z\","
			+ "\"place\":{\"latitude\":52.416117,\"longitude\":-4.083803},"
			+ "\"attendees\":[{\"id\":" + user1.id + ",\"id\":"+user2.id+"}],"
			+ "\"title\":\"Meeting title\","
			+ "\"description\":\"Meeting description\","
			+ "\"type\":\"Meeting type\"}";
		
		Http.Request request = newRequest();
		request.params.put("body", body);
		
		response = POST(request, BASE_CONTROLLER_PATH + user1Query, "application/json; charset=UTF-8", body);
		
		assertStatus(201, response);
	}
	
	@Test
	public void testShow() {
		testCreate();
		
		MeetingDTO meetingDTO = MeetingAssembler.meetingDTOWithJsonString(response.out.toString());
    	
		response = GET(BASE_CONTROLLER_PATH + "/" + meetingDTO.id + user1Query);
		assertIsOk(response);
	}
	
	@Test
	public void testShowUnrelatedMeeting() {
		testCreate();
		
		MeetingDTO meetingDTO = MeetingAssembler.meetingDTOWithJsonString(response.out.toString());
    	
		response = GET(BASE_CONTROLLER_PATH + "/" + meetingDTO.id + user3Query);
		assertIsNotFound(response);
	}
	
	@Test
	public void testUpdateMeeting() {
		testCreate();
		
    	MeetingDTO meetingDTO = MeetingAssembler.meetingDTOWithJsonString(response.out.toString());
		
    	String body = "{\"id\":" + meetingDTO.id + "," 
			+ "\"time\":\"2011-06-01T12:00:00Z\","
			+ "\"place\":{\"latitude\":52.416117,\"longitude\":-4.083803},"
			+ "\"attendees\":[{\"id\":" + user1.id + ",\"id\":"+user2.id+"}],"
			+ "\"title\":\"Meeting title updated\","
			+ "\"description\":\"Meeting description\","
			+ "\"type\":\"Meeting type\"}";
		
		Http.Request request = newRequest();
		request.params.put("body", body);
		
		response = PUT(request, BASE_CONTROLLER_PATH + "/" + meetingDTO.id + user1Query, "application/json; charset=UTF-8", body);
		meetingDTO = MeetingAssembler.meetingDTOWithJsonString(response.out.toString());
		
		assertIsOk(response);
		assertEquals("Meeting title updated", meetingDTO.title);
		//TODO: test updating all attributes on meeting (except owner)
	}
	
	/**
	 * Tests that when a user tries to update a meeting that they are not the owner of, the result is a 400.
	 */
	@Test
	public void testUpdateMeetingNonAuthUser() {
		testCreate();
		
    	MeetingDTO meetingDTO = MeetingAssembler.meetingDTOWithJsonString(response.out.toString());
		
    	String body = "{\"id\":" + meetingDTO.id + "," 
			+ "\"time\":\"2011-06-01T12:00:00Z\","
			+ "\"place\":{\"latitude\":52.416117,\"longitude\":-4.083803},"
			+ "\"attendees\":[{\"id\":" + user1.id + ",\"id\":"+user2.id+"}],"
			+ "\"title\":\"Meeting title updated\","
			+ "\"description\":\"Meeting description\","
			+ "\"type\":\"Meeting type\"}";
		
		Http.Request request = newRequest();
		request.params.put("body", body);
		
		response = PUT(request, BASE_CONTROLLER_PATH + "/" + meetingDTO.id + user2Query, "application/json; charset=UTF-8", body);
		assertStatus(400, response);
	}
	
	/**
	 * Tests that when the id in the URL and the id in the JSON body do not match, the result is a 404.
	 */
	@Test
	public void testUpdateMeetingMismatchedIds() {
		testCreate();
		
    	MeetingDTO meetingDTO = MeetingAssembler.meetingDTOWithJsonString(response.out.toString());
		
    	String body = "{\"id\":" + meetingDTO.id + "," 
			+ "\"time\":\"2011-06-01T12:00:00Z\","
			+ "\"place\":{\"latitude\":52.416117,\"longitude\":-4.083803},"
			+ "\"attendees\":[{\"id\":" + user1.id + ",\"id\":"+user2.id+"}],"
			+ "\"title\":\"Meeting title updated\","
			+ "\"description\":\"Meeting description\","
			+ "\"type\":\"Meeting type\"}";
		
		Http.Request request = newRequest();
		request.params.put("body", body);
		
		response = PUT(request, BASE_CONTROLLER_PATH + "/" + 0 + user1Query, "application/json; charset=UTF-8", body);
		assertStatus(404, response);
	}
	
	@Test
	public void testAcceptMeetingRequest() {
		testCreate();
		
		MeetingDTO meetingDTO = MeetingAssembler.meetingDTOWithJsonString(response.out.toString());
    	
		response = POST(BASE_CONTROLLER_PATH + "/" + meetingDTO.id + "/accept" + user2Query);
		assertIsOk(response);
		
		// Verify the rsvp was updated
		response = GET(BASE_CONTROLLER_PATH + "/" + meetingDTO.id + user2Query);
		assertIsOk(response);
		
		meetingDTO = MeetingAssembler.meetingDTOWithJsonString(response.out.toString());
    	for (AttendeeDTO attendee : meetingDTO.attendees) {
    		if (attendee.id.equals(user2.id)) {
    			assertEquals(MeetingResponse.YES, attendee.rsvp);
    		}
    	}
	}
	
	@Test
	public void testDeclineMeetingRequest() {
		testCreate();
		
		MeetingDTO meetingDTO = MeetingAssembler.meetingDTOWithJsonString(response.out.toString());
    	
		response = POST(BASE_CONTROLLER_PATH + "/" + meetingDTO.id + "/decline" + user2Query);
		assertIsOk(response);
		
		// Verify the rsvp was updated
		response = GET(BASE_CONTROLLER_PATH + "/" + meetingDTO.id + user2Query);
		assertIsOk(response);
		
		meetingDTO = MeetingAssembler.meetingDTOWithJsonString(response.out.toString());
    	for (AttendeeDTO attendee : meetingDTO.attendees) {
    		if (attendee.id.equals(user2.id)) {
    			assertEquals(MeetingResponse.NO, attendee.rsvp);
    		}
    	}
	}
	
	@Test
	public void testDeleteMeeting() {
		testCreate();
		
		MeetingDTO meetingDTO = MeetingAssembler.meetingDTOWithJsonString(response.out.toString());
    	
		response = DELETE(BASE_CONTROLLER_PATH + "/" + meetingDTO.id + "/" + user1Query);
		assertIsOk(response);
		
		// Verify the meeting was deleted
		response = GET(BASE_CONTROLLER_PATH + "/" + meetingDTO.id + user1Query);
		assertStatus(404, response);
	}
	
	@Test
	public void testDeleteMeetingNotFound() {   	
		response = DELETE(BASE_CONTROLLER_PATH + "/" + 0 + "/" + user1Query);
		assertStatus(404, response);
	}
	
	@Test
	public void testDeleteMeetingNonAuthUser() {
		testCreate();
		
		MeetingDTO meetingDTO = MeetingAssembler.meetingDTOWithJsonString(response.out.toString());
    	
		response = DELETE(BASE_CONTROLLER_PATH + "/" + meetingDTO.id + "/" + user2Query);
		assertStatus(400, response);
	}
	
}
