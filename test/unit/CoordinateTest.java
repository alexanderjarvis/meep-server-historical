package unit;

import models.Coordinate;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

/**
 * Tests the integrity of the Coordinate model.
 * 
 * @see Coordinate
 * @author Alex Jarvis axj7@aber.ac.uk
 */
public class CoordinateTest extends UnitTest {
	
	Double latitude = new Double(52.416117);
	Double longitude = new Double(-4.083803);
	
	@Before
	public void setUp() {
		Fixtures.deleteDatabase();
	}
	
	@Test
	public void testCreateCoordinate() {
		Coordinate coordinate = new Coordinate(latitude, longitude);
		coordinate.create();
		assertTrue(coordinate.isPersistent());
	}
	
	@Test
	public void testFindCoordinate() {
		testCreateCoordinate();
		Coordinate coordinate = Coordinate.find("byLatitudeAndLongitude", latitude, longitude).first();
		assertNotNull(coordinate);
		assertEquals(coordinate.getLatitude(), latitude);
		assertEquals(coordinate.getLongitude(), longitude);
	}
	
	@Test(expected=javax.persistence.PersistenceException.class)
	public void testUniqueConstraint() {
		testCreateCoordinate();
		testCreateCoordinate();
	}

}
