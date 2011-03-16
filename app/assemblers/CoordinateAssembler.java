package assemblers;

import models.Coordinate;
import DTO.CoordinateDTO;

/**
 * Assembler for the CoordinateDTO and Coordinate classes.
 * 
 * @see CoordinateDTO
 * @see Coordinate
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class CoordinateAssembler {
	
	/**
	 * Writes a CoordinateDTO from a Coordinate object.
	 * @param coordinate
	 * @return
	 */
	public static CoordinateDTO writeDTO(Coordinate coordinate) {
		if (coordinate != null) {
			CoordinateDTO coordinateDTO = new CoordinateDTO();
			coordinateDTO.id = coordinate.id;
			coordinateDTO.latitude = coordinate.latitude;
			coordinateDTO.longitude = coordinate.longitude;
			return coordinateDTO;
		}
		return null;
	}
	
	/**
	 * Creates a Coordinate from a CoordinateDTO
	 * @param coordinateDTO
	 * @return
	 */
	public static Coordinate createCoordinate(CoordinateDTO coordinateDTO) {
		Coordinate coordinate = new Coordinate(coordinateDTO.latitude, coordinateDTO.longitude);
		coordinate.create();
		return coordinate;
	}
	
	/**
	 * Updates a Coordinate using a CoordinateDTO
	 * @param coordinateDTO
	 * @return
	 */
	public static Coordinate updateCoordinate(CoordinateDTO coordinateDTO) {
		Coordinate coordinate = Coordinate.findById(coordinateDTO.id);
		if (coordinate != null) {
			coordinate.latitude = coordinateDTO.latitude;
			coordinate.longitude = coordinateDTO.longitude;
			coordinate.save();
		}
		return coordinate;
	}
}
