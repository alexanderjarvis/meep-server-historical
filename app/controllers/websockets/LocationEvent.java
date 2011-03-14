package controllers.websockets;

import DTO.RecentUserLocationsDTO;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public abstract class LocationEvent {
	
	public static class Connect extends LocationEvent {
		
	}

	public static class Disconnect extends LocationEvent {
	    
	}
	
	public static class OtherUserUpdated extends LocationEvent {
		
		RecentUserLocationsDTO locations = null;
		
		public OtherUserUpdated(RecentUserLocationsDTO locations) {
			this.locations = locations;
		}
	}
    
}

