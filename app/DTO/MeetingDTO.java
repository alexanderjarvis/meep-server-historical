package DTO;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class MeetingDTO {
	
	public Long id;
	
	@Required
	public DateTime time;
	
	@Required
	public CoordinateDTO place;
	
	public List<AttendeeDTO> attendees;
	
	public UserSummaryDTO owner;
	
	@Required
	public String title;
	
	public String description;

}
