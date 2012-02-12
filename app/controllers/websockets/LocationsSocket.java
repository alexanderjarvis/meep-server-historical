package controllers.websockets;

import static play.libs.F.Matcher.ClassOf;
import static play.libs.F.Matcher.Equals;
import static play.mvc.Http.WebSocketEvent.SocketClosed;
import static play.mvc.Http.WebSocketEvent.TextFrame;

import java.util.List;
import java.util.Timer;

import oauth2.CheckUserAuthentication;
import oauth2.OAuth2Constants;
import play.Logger;
import play.libs.F.E3;
import play.libs.F.EventStream;
import play.libs.F.Promise;
import play.mvc.WebSocketController;
import play.mvc.Http.WebSocketClose;
import play.mvc.Http.WebSocketEvent;
import utils.GsonFactory;
import DTO.UserDTO;
import DTO.UserLocationDTO;
import assemblers.UserLocationAssembler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The WebSocket Controller class which handles sending and receiving
 * location update events to/from clients.
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class LocationsSocket extends WebSocketController {	
	
	public static void connect() {
		
		Logger.info("WebSocket opened");
		
		// If not a valid token then disconnect the stream
		CheckUserAuthentication userAuth = new CheckUserAuthentication();
		UserDTO currentUserDTO = null;
		if (!userAuth.validToken(params.get(OAuth2Constants.PARAM_OAUTH_TOKEN))) {
			disconnect();
		} else {
			currentUserDTO = userAuth.getAuthorisedUserDTO();
		}
        
        // Socket connected, get the location stream and events for this user.
		LocationStreamManager locationStreamManager = LocationStreamManager.getInstance();
		EventStream<LocationEvent> locationStream = locationStreamManager.getLocationStreamForUserWithId(currentUserDTO.id);
		
        // Create a new heartbeat stream for this socket
        EventStream<HeartbeatEvent> heartbeatStream = new EventStream<HeartbeatEvent>();
        
        // Schedule a heartbeat event every 10 seconds
        HeartbeatTask heartbeatMonitor = new HeartbeatTask(heartbeatStream);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(heartbeatMonitor, 10000, 10000);
		
		// Loop while the socket is open
        while(inbound.isOpen()) {      	
        	
        	E3<WebSocketEvent, HeartbeatEvent, LocationEvent> e = await(Promise.waitEither(
        		inbound.nextEvent(),
        		heartbeatStream.nextEvent(),
        		locationStream.nextEvent()
        	));
        	
        	// Case: The socket has been closed
            for(WebSocketClose closed : SocketClosed.match(e._1)) {
            	Logger.info("WebSocket closed");
            	locationStreamManager.closeStreamForUserWithId(currentUserDTO.id);
                disconnect();
            }
            
            // Case: HeartbeatEvent received (from client)
            for(String text: TextFrame.and(Equals(MessageWrapper.wrap("~h~PONG"))).match(e._1)) {
            	Logger.debug("Heartbeat received");
            	heartbeatMonitor.setResponse(true);
            }
            
            // Case: HeartbeatEvent.Pulse received (from heartbeatMonitor)
            for (HeartbeatEvent event : ClassOf(HeartbeatEvent.Pulse.class).match(e._2)) {
            	Logger.debug("HeartbeatEvent Pulse from timer");
            	heartbeatMonitor.setResponse(false);
            	outbound.send(MessageWrapper.wrap("~h~PING"));
            	Logger.debug("Heartbeat sent");
            }
            
            // Case: HeartbeatEvent.Dead received (from heartbeatMonitor)
            for (HeartbeatEvent event : ClassOf(HeartbeatEvent.Dead.class).match(e._2)) {
            	Logger.debug("HeartbeatEvent Dead from timer");
            	disconnect();
            }
            
            // Case: Update location message sent from client (only 1 type of message can be sent - location update).
            // This message is sent as a Json Array and so we can specify that it will always start with '['.
            for(String message: TextFrame.match(e._1)) {
            	String unwrappedMessage = MessageWrapper.unwrap(message);
            	if (isJson(unwrappedMessage)) {
            		String jsonString = removeJsonHeader(unwrappedMessage);
	            	Logger.debug("WebSocket message received:" + jsonString);
	            	JsonArray jsonArray = stringToJsonArray(jsonString);
	            	if (jsonArray != null && jsonArray.isJsonArray()) {
	            		// Obtain DTOs from the JsonArray
	            		List<UserLocationDTO> userLocationDTOs = UserLocationAssembler.userLocationDTOsWithJsonArray(jsonArray);
	            		// Persist locations with the DTOs
	            		List<UserLocationDTO> createdUserLocationDTOs = UserLocationAssembler.createUserLocations(userLocationDTOs, currentUserDTO);
	            		LocationStreamHelper.publishNewUserLocations(createdUserLocationDTOs, currentUserDTO);
	            	}
            	}
            }
                        
            // Case: Another user has updated their locations and it is on this users stream.
            for(LocationEvent.OtherUserUpdated otherUserUpdated : ClassOf(LocationEvent.OtherUserUpdated.class).match(e._3)) {
            	
            	String jsonString = objectToJsonString(otherUserUpdated.locations);
            	Logger.debug("WebSocket sending:\n" + jsonString);
            	outbound.send(MessageWrapper.wrap("~j~" + jsonString));
            }
            
            
        } // end while socket open
        
	}
	
	private static boolean isJson(String message) {
		return MessageWrapper.unwrap(message).startsWith("~j~");
	}
	
	private static String removeJsonHeader(String message) {
		return message.substring(3);
	}
	
	/**
	 * Returns a JSON string from an Object. This is usually handled by the RenderJSON method
	 * of play.mvc.Controller
	 * 
	 * There is a convenience method for WebSocketControllers (outbound.sendJson) - but this
	 * method does not let you control the behaviour of the created Gson object (as I need to
	 * specify the date format) and it cannot be overidden in this class because its an internal
	 * class inside play.mvc.Http.
	 * 
	 * @param object
	 * @return
	 * @see play.mvc.Controller
	 * @see play.mvc.Http
	 */
	private static String objectToJsonString(Object object) {
		return GsonFactory.gsonBuilder().create().toJson(object);
	}
	
	/**
	 * Returns a JsonArray from a String. Usually this is done automatically by GsonArrayBinder
	 * for a normal play.mvc.Controller class.
	 *  
	 * @param jsonArray
	 * @return
	 * @see play.mvc.Controller
	 */
	private static JsonArray stringToJsonArray(String jsonArray) {
		return (JsonArray) new JsonParser().parse(jsonArray);
	}
}

