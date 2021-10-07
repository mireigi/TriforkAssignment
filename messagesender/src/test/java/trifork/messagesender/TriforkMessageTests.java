package trifork.messagesender;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import trifork.messagesender.model.TriforkMessage;

@SpringBootTest
class TriforkMessageTests {

	@Test
	void testBlankContentThrows() {
        assertThrows(IllegalArgumentException.class, () -> TriforkMessage.Create(" "));
	}

	@Test
	void testEmptyContentThrows() {
        assertThrows(IllegalArgumentException.class, () -> TriforkMessage.Create(""));
	}

	@Test
	void testNullContentThrows() {
        assertThrows(IllegalArgumentException.class, () -> TriforkMessage.Create(null));
	}

	@Test
	void testValidContentDoesNotThrow() {
        assertDoesNotThrow(() -> TriforkMessage.Create("Hello World!"));
	}
}
