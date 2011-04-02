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
			coordinateDTO.latitude = coordinate.getLatitude();
			coordinateDTO.longitude = coordinate.getLongitude();
			return coordinateDTO;
		}
		return null;
	}
	
	/**
	 * Creates and returns a Coordinate from a CoordinateDTO
	 * @param coordinateDTO
	 * @return
	 */
	public static Coordinate createCoordinate(CoordinateDTO coordinateDTO) {
		return createOrUpdateCoordinate(coordinateDTO);
	}
	
	/**
	 * Updates and returns a Coordinate using a CoordinateDTO
	 * @param coordinateDTO
	 * @return
	 */
	public static Coordinate updateCoordinate(CoordinateDTO coordinateDTO) {
		return createOrUpdateCoordinate(coordinateDTO);
	}
	
	/**
	 * Creating and updating a Coordinate has the same logic because the unique key constraint requires
	 * that the application check for an existing Coordinate before creating a new one and similarly when
	 * updating a Coordinate.
	 * @param coordinateDTO
	 * @return
	 */
	private static Coordinate createOrUpdateCoordinate(CoordinateDTO coordinateDTO) {
		Coordinate coordinate = Coordinate.find("byLatitudeAndLongitude", coordinateDTO.latitude, coordinateDTO.longitude).first();
		if (coordinate == null) {
			coordinate = new Coordinate(coordinateDTO.latitude, coordinateDTO.longitude);
			coordinate.create();
		}
		return coordinate;
	}
}
