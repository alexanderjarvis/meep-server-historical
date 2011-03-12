package controllers.websockets;

import java.util.List;

import play.libs.F.ArchivedEventStream;
import play.libs.F.EventStream;
import play.libs.F.IndexedEvent;
import play.libs.F.Promise;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class LocationStream {
	
	private static LocationStream INSTANCE = null;
	
	final ArchivedEventStream<LocationStream.Event> locationEvents = new ArchivedEventStream<LocationStream.Event>(100);
	
	/**
	 * Singleton
	 * @return
	 */
	public static LocationStream getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new LocationStream();
		}
		return INSTANCE;
	}
	
	/**
     * When a user joins the stream.
     */
    public EventStream<LocationStream.Event> join(String user) {
        locationEvents.publish(new Join(user));
        return locationEvents.eventStream();
    }
    
    /**
     * A user leaves the stream
     */
    public void leave(String user) {
        locationEvents.publish(new Leave(user));
    }
    
    /**
     * A user say something on the room
     */
    public void say(String user, String text) {
        if(text == null || text.trim().equals("")) {
            return;
        }
        locationEvents.publish(new Message(user, text));
    }
    
    public static abstract class Event {
        
        final public String type;
        final public Long timestamp;
        
        public Event(String type) {
            this.type = type;
            this.timestamp = System.currentTimeMillis();
        }
        
    }
    
    public static class Join extends Event {
        
        final public String user;
        
        public Join(String user) {
            super("join");
            this.user = user;
        }
        
    }
    
    public static class Leave extends Event {
        
        final public String user;
        
        public Leave(String user) {
            super("leave");
            this.user = user;
        }
        
    }
    
    public static class Message extends Event {
        
        final public String user;
        final public String text;
        
        public Message(String user, String text) {
            super("message");
            this.user = user;
            this.text = text;
        }
        
    }
    
}
