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
	void create_WhenContentIsBlank_Throws() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> TriforkMessage.create(" ", new Timestamp(System.currentTimeMillis()))
        );
        assertEquals("Content cannot be blank, empty, or null.", exception.getMessage());
	}

	@Test
	void create_WhenContentIsEmpty_Throws() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> TriforkMessage.create("", new Timestamp(System.currentTimeMillis()))
        );
        assertEquals("Content cannot be blank, empty, or null.", exception.getMessage());
	}

	@Test
	void create_WhenContentIsNull_Throws() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> TriforkMessage.create(null, new Timestamp(System.currentTimeMillis()))
        );
        assertEquals("Content cannot be blank, empty, or null.", exception.getMessage());
	}

	@Test
	void create_WhenTimestampIsNull_Throws() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> TriforkMessage.create("Hello World!", null)
        );
        assertEquals("Timestamp cannot be null.", exception.getMessage());
	}

	@Test
	void create_WhenContentIsValid_DoesNotThrow() {
        assertDoesNotThrow(() -> TriforkMessage.create("Hello World!", new Timestamp(System.currentTimeMillis())));
	}
}
