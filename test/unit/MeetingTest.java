package unit;

import models.Attendee;
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
		Fixtures.load("MeetingTestData.yml");
		meeting = Meeting.all().first();
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
	
	/****
	 * Attendees
	 */
	
	@Test
	public void testAttendeesSize() {
		assertEquals(2, meeting.attendees.size());
	}
	
	@Test
	public void testAddAttendee() {
		Attendee attendee = new Attendee();
		User user = User.all().first();
		attendee.user = user;
		attendee.rsvp = MeetingResponse.YES;
		meeting.attendees.add(attendee);
		assertEquals(3, meeting.attendees.size());
	}
	
	@Test
	public void testRemoveAttendee() {
		meeting.attendees.remove(0);
	}

}
