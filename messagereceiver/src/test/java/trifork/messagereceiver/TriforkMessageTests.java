package trifork.messagereceiver;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import trifork.messagereceiver.model.TriforkMessage;

@SpringBootTest
class TriforkMessageTests {

	@Test
	void testBlankContentThrows() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> TriforkMessage.Create(" ", new Timestamp(System.currentTimeMillis()))
        );
        assertEquals("Content cannot be blank, empty, or null.", exception.getMessage());
	}

	@Test
	void testEmptyContentThrows() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> TriforkMessage.Create("", new Timestamp(System.currentTimeMillis()))
        );
        assertEquals("Content cannot be blank, empty, or null.", exception.getMessage());
	}

	@Test
	void testNullContentThrows() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> TriforkMessage.Create(null, new Timestamp(System.currentTimeMillis()))
        );
        assertEquals("Content cannot be blank, empty, or null.", exception.getMessage());
	}

	@Test
	void testNullTimestampThrows() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> TriforkMessage.Create("Hello World!", null)
        );
        assertEquals("Timestamp cannot be null.", exception.getMessage());
	}

	@Test
	void testValidContentDoesNotThrow() {
        assertDoesNotThrow(() -> TriforkMessage.Create("Hello World!", new Timestamp(System.currentTimeMillis())));
	}
}
