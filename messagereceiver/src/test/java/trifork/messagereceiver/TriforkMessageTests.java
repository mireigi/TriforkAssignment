package trifork.messagereceiver;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import trifork.messagereceiver.model.TriforkMessage;

@SpringBootTest
class TriforkMessageTests {
        
    @Test
    void create_WhenContentIsBlank_Throws() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> TriforkMessage.create(" ", Instant.now())
        );
        assertEquals("Content cannot be blank, empty, or null.", exception.getMessage());
    }

    @Test
    void create_WhenContentIsEmpty_Throws() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> TriforkMessage.create("", Instant.now())
        );
        assertEquals("Content cannot be blank, empty, or null.", exception.getMessage());
    }

    @Test
    void create_WhenContentIsNull_Throws() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> TriforkMessage.create(null, Instant.now())
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
        assertDoesNotThrow(() -> TriforkMessage.create("Hello World!", Instant.now()));
    }

    @Test
    void isOutdated_WhenOlderThanOneMinute_ReturnsTrue() {
        Instant timestampNow = Instant.now();
        Instant timestampPreviously = timestampNow.minusSeconds(61);
        TriforkMessage sut = TriforkMessage.create("Lorem Ipsum", timestampPreviously);

        assertTrue(sut.isOutdated(timestampNow));
    }

    @Test
    void isOutdated_WhenYoungerThanOneMinute_ReturnsFalse() {
        Instant timestampNow = Instant.now();
        Instant timestampPreviously = timestampNow.minusSeconds(59);
        TriforkMessage sut = TriforkMessage.create("Lorem Ipsum", timestampPreviously);

        assertFalse(sut.isOutdated(timestampNow));
    }

    @Test
    void isOutdated_WhenExactlyOneMinuteOld_ReturnsFalse() {
        Instant timestampNow = Instant.now();
        Instant timestampPreviously = timestampNow.minusSeconds(60);
        TriforkMessage sut = TriforkMessage.create("Lorem Ipsum", timestampPreviously);

        assertFalse(sut.isOutdated(timestampNow));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, 7, 9})
    void isTimestampOdd_WhenTimestampIsOdd_ReturnsTrue(int second) {
        Instant timestampToUse = Instant.parse(String.format("2021-10-08T21:15:0%d.000Z", second));
        TriforkMessage sut = TriforkMessage.create("Lorem Ipsum", timestampToUse);

        assertTrue(sut.isTimestampOdd());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 2, 4, 6, 8})
    void isTimestampOdd_WhenTimestampIsEven_ReturnsFalse(int second) {
        Instant timestampToUse = Instant.parse(String.format("2021-10-08T21:15:0%d.000Z", second));
        TriforkMessage sut = TriforkMessage.create("Lorem Ipsum", timestampToUse);

        assertFalse(sut.isTimestampOdd());
    }
}
