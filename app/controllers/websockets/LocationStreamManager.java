package controllers.websockets;

import java.util.HashMap;
import java.util.Map;

import models.User;
import play.Logger;
import play.libs.F.EventStream;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class LocationStreamManager {
	
	private static LocationStreamManager INSTANCE = null;
	
	// TODO: persist archived streams?
	private Map<Long, LocationEventStream<LocationEvent>> archivedStreams = new HashMap<Long, LocationEventStream<LocationEvent>>();
	
	private Map<Long, EventStream<LocationEvent>> connectedStreams = new HashMap<Long, EventStream<LocationEvent>>();
	
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
		
		if (connectedStreams.containsKey(id)) {
			connectedStreams.remove(id);
		} 
		
		if (archivedStreams.containsKey(id)) {
			locationStream = archivedStreams.get(id).eventStream();
			
		} else {
			LocationEventStream<LocationEvent> archivedStream = new LocationEventStream<LocationEvent>(100);
			archivedStreams.put(id, archivedStream);
			locationStream = archivedStream.eventStream();
		}
		
		if (locationStream != null) {
			connectedStreams.put(id, locationStream);
			return locationStream;
		}
		return null;
	}
	
	/**
	 * Publishes a location event to a specific users location stream.
	 * 
	 * @param user
	 * @param locationEvent
	 */
	public void publishLocationEventToUserWithId(Long id, LocationEvent locationEvent) {
		
		
		LocationEventStream<LocationEvent> archiveStreamForUser = null;
		if (!archivedStreams.containsKey(id)) {
			archiveStreamForUser = new LocationEventStream<LocationEvent>(100);
			archivedStreams.put(id, archiveStreamForUser);
				
		}
		
		archiveStreamForUser = archivedStreams.get(id);
		archiveStreamForUser.publish(locationEvent);
	}
	
	/**
	 * 
	 * @param id
	 */
	public void closeStreamForUserWithId(Long id) {
		if (connectedStreams.containsKey(id)) {
			Logger.info("Removing connected stream with user id: " + id);
			EventStream<LocationEvent> connectedStream = connectedStreams.get(id);
			connectedStreams.remove(id);
			archivedStreams.get(id).removePipedStream(connectedStream);
		}
	}

}
