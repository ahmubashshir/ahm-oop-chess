package bd.ac.miu.cse.b60.oop.ahm.chess;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.lang.IllegalArgumentException;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Coord class.
 * These tests verify that invalid coordinates throw exceptions,
 * and valid coordinates are accepted.
 */
public class AppTest {

	@Test
	void testCoord() {
		// Invalid column and row
		assertThrowsExactly(IllegalArgumentException.class, () ->
		                    new Coord('1', '1')
		                   );
		// Invalid column, valid row
		assertThrowsExactly(IllegalArgumentException.class, () ->
		                    new Coord('1', 'h')
		                   );
		// Valid lower bound
		assertDoesNotThrow(() -> new Coord('a', '1'));
		// Valid upper bound
		assertDoesNotThrow(() -> new Coord('h', '8'));
	}
}
