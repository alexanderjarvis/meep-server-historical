package controllers.websockets;

import java.util.TimerTask;

import play.libs.F.EventStream;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class HeartbeatTask extends TimerTask {
	
	private EventStream<HeartbeatEvent> heartbeatStream = null;
	private boolean response = true;
	
	public HeartbeatTask(EventStream<HeartbeatEvent> heartbeatStream) {
		this.heartbeatStream = heartbeatStream;
	}

	@Override
	public void run() {
		if (response) {
			heartbeatStream.publish(new HeartbeatEvent.Pulse());
		} else {
			heartbeatStream.publish(new HeartbeatEvent.Dead());
		}
	}
	
	public boolean isResponse() {
		return this.response;
	}
	
	public void setResponse(boolean response) {
		this.response = response;
	}

}
