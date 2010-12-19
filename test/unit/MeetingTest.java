package unit;

import java.util.Date;

import models.Attendee;
import models.Coordinate;
import models.Meeting;
import models.User;
import models.Attendee.MeetingResponse;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class MeetingTest extends UnitTest {
	
	Meeting meeting;
	
	@Before
	public void setup() {
		Fixtures.deleteAll();
		
		User owner = new User();
		Date today = new Date();
		Coordinate place = new Coordinate(new Double(2), new Double(2));
		
		meeting = new Meeting(today, place, owner);
	}
	
	/**
	 * If the JPA are not used on the model classes, then they will return null objects.
	 */
	@Test
	public void testRelations() {
		assertNotNull(meeting.place);
		assertNotNull(meeting.attendees);
		assertNotNull(meeting.owner);
	}
	
	/**
	 * Attendees
	 */
	@Test
	public void testAttendeesSize() {
		assertEquals(0, meeting.attendees.size());
	}
	
	@Test
	public void testAddAttendee() {
		User user = new User();
		
		int sizeBefore = meeting.attendees.size();
		meeting.addAttendee(user);
		int sizeAfter = meeting.attendees.size();
		
		assertTrue(sizeAfter > sizeBefore);
	}
	
	@Test
	public void testRemoveAttendee() {
		testAddAttendee();
		meeting.attendees.remove(0);
	}
	
	@Test
	public void testSetRSVP() {
		testAddAttendee();
		assertTrue(meeting.setAttendeeRSVP(meeting.attendees.get(0).getUser(), MeetingResponse.YES));
	}

}
