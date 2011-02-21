package DTO;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class MeetingDTO {
	
	public Long id;
	
	public Date time;
	
	public CoordinateDTO place;
	
	public List<AttendeeDTO> attendees;
	
	public UserSummaryDTO owner;
	
	public String title;
	
	public String description;
	
	public String type;

}
