package controllers.websockets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import play.libs.F.EventStream;
import play.libs.F.IndexedEvent;
import play.libs.F.Promise;

public class LocationEventStream<T> {
	
	final int archiveSize;
    final ConcurrentLinkedQueue<IndexedEvent<T>> events = new ConcurrentLinkedQueue<IndexedEvent<T>>();
    final List<EventStream<T>> pipedStreams = new ArrayList<EventStream<T>>();

    public LocationEventStream(int archiveSize) {
        this.archiveSize = archiveSize;
    }

    public synchronized EventStream<T> eventStream() {
        final EventStream<T> stream = new EventStream<T>(archiveSize);
        for (IndexedEvent<T> event : events) {
            stream.publish(event.data);
        }
        pipedStreams.add(stream);
        return stream;
    }

    public List<T> archive() {
        List<T> result = new ArrayList<T>();
        for (IndexedEvent<T> event : events) {
            result.add(event.data);
        }
        return result;
    }

    public synchronized void publish(T event) {
        if (events.size() >= archiveSize) {
            events.poll();
        }
        events.offer(new IndexedEvent(event));
        for (EventStream<T> eventStream : pipedStreams) {
            eventStream.publish(event);
        }
    }
    
    public boolean removePipedStream(EventStream pipedStream) {
    	return pipedStreams.remove(pipedStream);
    }
}
