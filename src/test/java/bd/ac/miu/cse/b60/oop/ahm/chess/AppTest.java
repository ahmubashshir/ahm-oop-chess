package bd.ac.miu.cse.b60.oop.ahm.chess;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.lang.IllegalArgumentException;
import org.junit.jupiter.api.Test;

public class AppTest {

	@Test
	void testCoord() {
		assertThrowsExactly(IllegalArgumentException.class, () ->
		                    new Coord('1', '1')
		                   );
		assertThrowsExactly(IllegalArgumentException.class, () ->
		                    new Coord('1', 'h')
		                   );
		assertDoesNotThrow(() -> new Coord('a', '1'));
		assertDoesNotThrow(() -> new Coord('h', '8'));
	}
}
