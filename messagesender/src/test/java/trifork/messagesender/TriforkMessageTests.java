package trifork.messagesender;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import trifork.messagesender.model.TriforkMessage;

@SpringBootTest
class TriforkMessageTests {

	@Test
	void create_WhenContentIsBlank_Throws() {
        assertThrows(IllegalArgumentException.class, () -> TriforkMessage.create(" "));
	}

	@Test
	void create_WhenContentIsEmpty_Throws() {
        assertThrows(IllegalArgumentException.class, () -> TriforkMessage.create(""));
	}

	@Test
	void create_WhenContentIsNull_Throws() {
        assertThrows(IllegalArgumentException.class, () -> TriforkMessage.create(null));
	}

	@Test
	void create_WhenContentIsValid_DoesNotThrow() {
        assertDoesNotThrow(() -> TriforkMessage.create("Hello World!"));
	}
}
