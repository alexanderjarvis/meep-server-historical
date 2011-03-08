package DTO;

import java.util.Date;

import play.data.validation.Required;

import models.Coordinate;
import models.User;

public class UserLocationDTO {
	
	@Required
	public Date time;
	
	@Required
	public CoordinateDTO coordinate;
	
	public Double speed;
	
	public Double altitude;
	
	public Double trueHeading;
	
	public Double verticalAccuracy;
	
	public Double horizontalAccuracy;

}
