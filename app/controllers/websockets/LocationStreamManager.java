package controllers.websockets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.libs.F.ArchivedEventStream;
import play.libs.F.EventStream;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class LocationStreamManager {
	
	private static LocationStreamManager INSTANCE = null;
	
	private Map<Long, ArchivedEventStream<LocationEvent>> archivedLocationStreams = new HashMap<Long, ArchivedEventStream<LocationEvent>>();
	private Map<Long, EventStream<LocationEvent>> currentLocationStreams = new HashMap<Long, EventStream<LocationEvent>>();
	
	/**
	 * Singleton
	 * @return
	 */
	public static LocationStreamManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new LocationStreamManager();
		}
		return INSTANCE;
	}
	
	/**
	 * Returns the location stream for a user and creates it if it does not already exist.
	 * 
	 * @param id
	 * @return
	 */
	public EventStream<LocationEvent> getLocationStreamForUserWithId(Long id) {
		EventStream<LocationEvent> locationStream = null;
		
		// If location stream is
		if (currentLocationStreams.containsKey(id)) {
			locationStream = currentLocationStreams.get(id);
		} else {
			locationStream = new EventStream<LocationEvent>(100);
			currentLocationStreams.put(id, locationStream);
		}
		
		if (locationStream != null) {
			return locationStream;
		}
		return null;
	}
	
	/**
	 * Publishes a location event to a specific users location stream.
	 * 
	 * @param id
	 * @param locationEvent
	 */
	public void publishLocationEventToUserWithId(Long id, LocationEvent locationEvent) {
		EventStream<LocationEvent> locationStream = getLocationStreamForUserWithId(id);
		locationStream.publish(locationEvent);
	}

}
