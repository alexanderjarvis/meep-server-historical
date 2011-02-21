package unit;

import java.util.Date;

import models.Attendee;
import models.Coordinate;
import models.Meeting;
import models.User;
import models.Attendee.MeetingResponse;
import models.helpers.MeetingHelper;

import org.junit.Before;
import org.junit.Test;

import play.db.jpa.JPA;
import play.test.Fixtures;
import play.test.UnitTest;

public class MeetingTest extends UnitTest {
	
	Meeting meeting;
	User user;
	
	@Before
	public void setup() {
		Fixtures.deleteAll();
		
		User owner = new User();
		owner.save();
		Date today = new Date();
		Coordinate place = new Coordinate(new Double(2), new Double(2));
		place.save();
		
		meeting = new Meeting();
		meeting.time = today;
		meeting.place = place;
		meeting.owner = owner;
		meeting.save();
		
		user = new User();
		user.save();
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
		assertTrue(MeetingHelper.addAttendee(meeting, user));
		JPA.em().clear();
		
		meeting = Meeting.all().first();
		assertEquals(1, meeting.attendees.size());
		
		Attendee attendee = meeting.attendees.get(0);
		assertEquals(meeting, attendee.meeting);
		assertEquals(user, attendee.user);
	}
	
	@Test
	public void testRemoveAttendee() {
		testAddAttendee();
		assertTrue(MeetingHelper.removeAttendee(meeting, user));
		assertEquals(0, meeting.attendees.size());
	}
	
	@Test
	public void testSetRSVP() {
		testAddAttendee();
		User user = meeting.attendees.get(0).user;
		assertNotNull(user);
		assertTrue(MeetingHelper.setAttendeeRSVP(meeting, user, MeetingResponse.YES));
	}

}
