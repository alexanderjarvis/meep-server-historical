package controllers.websockets;

import static play.libs.F.Matcher.ClassOf;
import static play.libs.F.Matcher.Equals;
import static play.mvc.Http.WebSocketEvent.SocketClosed;
import static play.mvc.Http.WebSocketEvent.TextFrame;

import java.util.Timer;

import oauth2.CheckUserAuthentication;
import oauth2.OAuth2Constants;
import play.Logger;
import play.libs.F.Either3;
import play.libs.F.EventStream;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.WebSocketController;
import play.mvc.Http.WebSocketClose;
import play.mvc.Http.WebSocketEvent;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class LocationsSocket extends WebSocketController {	
	
	/**
	 * 
	 * @param message
	 * @return
	 */
	public static String wrapMessage(String message) {		
		return message.format("~m~%s~m~%s", Integer.toString(message.length()), message);
	}
	
	/**
	 * 
	 * @param message
	 * @return
	 */
	public static String unwrapMessage(String message) {
		return message.replaceFirst("~m~[0-9]+~m~", "");
	}
	
	/**
	 * 
	 */
	public static void connect() {
		
		// If not a valid token then disconnect the stream
		CheckUserAuthentication userAuth = new CheckUserAuthentication();
		if (!userAuth.validToken(params.get(OAuth2Constants.PARAM_OAUTH_TOKEN))) {
			disconnect();
		}
		
		// TODO: make multiple streams for different meetings / or users
		LocationStream locationStream = LocationStream.getInstance();
        
        // Socket connected, join the chat room
        EventStream<LocationStream.Event> locationMessagesStream = locationStream.join("user");
        
        //
        EventStream<HeartbeatEvent> heartbeatStream = new EventStream<HeartbeatEvent>();
        
        HeartbeatTask heartbeatMonitor = new HeartbeatTask(heartbeatStream);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(heartbeatMonitor, 10000, 10000);
		
		// Loop while the socket is open
        while(inbound.isOpen()) {      	
        	
        	Either3<WebSocketEvent, HeartbeatEvent, LocationStream.Event> e = await(Promise.waitEither(
        		inbound.nextEvent(),
        		heartbeatStream.nextEvent(),
        		locationMessagesStream.nextEvent()
        	));
        	
        	// Case: The socket has been closed
            for(WebSocketClose closed : SocketClosed.match(e._1)) {
            	Logger.info("web socket closed");
            	locationStream.leave("user");
                disconnect();
            }
            
            // Case: HeartbeatEvent received (from client)
            for(String text: TextFrame.and(Equals(wrapMessage("~h~PONG"))).match(e._1)) {
            	Logger.info("HeartbeatEvent received from client");
            	heartbeatMonitor.setResponse(true);
            }
            
            // Case: HeartbeatEvent.Pulse received (from heartbeatMonitor)
            for (HeartbeatEvent event : ClassOf(HeartbeatEvent.Pulse.class).match(e._2)) {
            	Logger.info("HeartbeatEvent Pulse from timer");
            	heartbeatMonitor.setResponse(false);
            	outbound.send(wrapMessage("~h~PING"));
            	Logger.info("Heartbeat sent");
            }
            
            // Case: HeartbeatEvent.Dead received (from heartbeatMonitor)
            for (HeartbeatEvent event : ClassOf(HeartbeatEvent.Dead.class).match(e._2)) {
            	Logger.info("HeartbeatEvent Dead from timer");
            	disconnect();
            }
            
            // Case: Someone joined the room
            for(LocationStream.Join joined: ClassOf(LocationStream.Join.class).match(e._2)) {
                outbound.send(wrapMessage("join:" + joined.user));
            }
            
            // Case: New message on the chat room
            for(LocationStream.Message message: ClassOf(LocationStream.Message.class).match(e._2)) {
                outbound.send("message:%s:%s", message.user, message.text);
            }
            
            // Case: Someone left the room
            for(LocationStream.Leave left: ClassOf(LocationStream.Leave.class).match(e._2)) {
                outbound.send("leave:%s", left.user);
            }
        } // end while socket open
        
	}
}

