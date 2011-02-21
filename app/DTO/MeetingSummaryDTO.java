package DTO;

import java.util.Date;
import java.util.List;

public class MeetingSummaryDTO {
	
	public Long id;
	
	public Date time;
	
	public CoordinateDTO place;
	
	public List<AttendeeDTO> attendees;
	
	public UserSummaryDTO owner;
	
	public String title;
	
	public String description;
	
	public String type;

}
