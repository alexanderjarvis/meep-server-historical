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

/**
 * Tests the functions of the Meeting and Attendee models to verify their integrity.
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class MeetingAndAttendeeTest extends UnitTest {
	
	Meeting meeting;
	User owner;
	User attendeeUser;
	
	@Before
	public void setup() {
		Fixtures.deleteDatabase();
		
		meeting = new Meeting();
		meeting.time = new Date();
		Coordinate coordinate = new Coordinate(new Double(52.416117), new Double(-4.083803));
		coordinate.create();
		meeting.place = coordinate;
		owner = new User();
		owner.create();
		meeting.owner = owner;
		meeting.title = "Meeting title";
		meeting.description = "Meeting description";
		meeting.create();
		
		// 2nd User to be added to the meeting as an attendee
		attendeeUser = new User();
		attendeeUser.create();
	}
	
	@Test
	public void testBasicAttributes() {
		
		Meeting sameMeeting = Meeting.findById(meeting.id);
		assertNotNull(sameMeeting);
		
		assertEquals(sameMeeting.time, meeting.time);
		assertEquals(sameMeeting.place, meeting.place);
		assertEquals(sameMeeting.owner, meeting.owner);
		assertEquals(sameMeeting.title, meeting.title);
		assertEquals(sameMeeting.description, meeting.description);
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
	
	@Test
	public void testAttendeesSize() {
		assertEquals(0, meeting.attendees.size());
	}
	
	@Test
	public void testAddAttendee() {
		MeetingHelper.createAttendee(meeting, attendeeUser);
		
		meeting = Meeting.all().first();
		assertEquals(1, meeting.attendees.size());
		
		Attendee attendee = meeting.attendees.get(0);
		assertEquals(meeting, attendee.meeting);
		assertEquals(attendeeUser, attendee.user);
	}
	
	@Test
	public void testRemoveAttendee() {
		testAddAttendee();
		assertTrue(MeetingHelper.removeAttendee(meeting, attendeeUser));
		assertEquals(0, meeting.attendees.size());
	}
	
	@Test
	public void testAcceptMeetingRequest() {
		testAddAttendee();
		Attendee attendee = meeting.attendees.get(0);
		assertTrue(MeetingHelper.acceptMeetingRequest(meeting, attendee.user));
		assertEquals(attendee.rsvp, MeetingResponse.YES);
	}
	
	@Test
	public void testDeclineMeetingRequest() {
		testAddAttendee();
		Attendee attendee = meeting.attendees.get(0);
		assertTrue(MeetingHelper.declineMeetingRequest(meeting, attendee.user));
		assertEquals(attendee.rsvp, MeetingResponse.NO);
	}
	
	@Test
	public void testUpdateAttendeesMinutesBefore() {
		testAddAttendee();
		Attendee attendee = meeting.attendees.get(0);
		assertTrue(MeetingHelper.updateAttendeesMinutesBefore(15, meeting, attendee.user));
		assertEquals(attendee.minutesBefore, new Integer(15));
	}
	
	@Test
	public void testUpdateAttendeesMinutesBeforeWhenNotAttendee() {
		User user = new User();
		user.create();
		assertFalse(MeetingHelper.updateAttendeesMinutesBefore(15, meeting, user));
	}
	
	@Test
	public void testDeleteMeeting() {
		
		MeetingHelper.createAttendee(meeting, attendeeUser);
		
		Long ownerId = meeting.owner.id;
		Long attendeeId = attendeeUser.meetingsRelated.get(0).id;
		
		meeting.delete();
		
		meeting = meeting.findById(meeting.id);
		assertNull(meeting);
		owner = owner.findById(ownerId);
		assertNotNull(owner);
		assertEquals(0, owner.meetingsCreated.size());
		assertEquals(0, owner.meetingsRelated.size());
		Attendee attendee = Attendee.findById(attendeeId);
		assertNull(attendee);
	}

}
