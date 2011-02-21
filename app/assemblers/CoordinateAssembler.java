package assemblers;

import models.Coordinate;
import DTO.CoordinateDTO;

/**
 * 
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class CoordinateAssembler {
	
	/**
	 * 
	 * @param coordinate
	 * @return
	 */
	public static CoordinateDTO writeDTO(Coordinate coordinate) {
		CoordinateDTO coordinateDTO = new CoordinateDTO();
		coordinateDTO.latitude = coordinate.latitude;
		coordinateDTO.longitude = coordinate.longitude;
		return coordinateDTO;
	}
	
	public static Coordinate createCoordinate(CoordinateDTO coordinateDTO) {
		Coordinate coordinate = new Coordinate(coordinateDTO.latitude, coordinateDTO.longitude);
		coordinate.save();
		return coordinate;
	}
}
