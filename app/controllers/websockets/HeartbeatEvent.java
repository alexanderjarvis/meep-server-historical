package controllers.websockets;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public abstract class HeartbeatEvent {
	
	/**
	 * 
	 */
	public static class Pulse extends HeartbeatEvent {
	}
	
	/**
	 * 
	 */
	public static class Dead extends HeartbeatEvent {
	}

}
