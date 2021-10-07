package trifork.messagesender;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import trifork.messagesender.model.TriforkMessage;
import trifork.messagesender.model.TriforkMessageFactory;

@SpringBootTest
class TriforkMessageFactoryTests {
    
    @Test
    void constructor_WhenAvailableWordsIsNull_Throws()
    {
        assertThrows(IllegalArgumentException.class, () -> new TriforkMessageFactory(null));
    }
    
    @Test
    void constructor_WhenAvailableWordsIsEmpty_Throws()
    {
        String[] availableWords = new String[] {};
        assertThrows(IllegalArgumentException.class, () -> new TriforkMessageFactory(availableWords));
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, -1, 0})
    void generate_WhenWordCountIsLessThanOne_Throws(int wordCount)
    {
        String[] availableWords = new String[] { "Lorem" };
        TriforkMessageFactory sut = new TriforkMessageFactory(availableWords);

        assertThrows(IllegalArgumentException.class, () -> sut.generate(wordCount));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 10, 100})
    void generate_ProvidesTriforkMessageWithCorrectWordCount(int expectedWordCount)
    {
        String[] availableWords = new String[] { "Lorem" };
        TriforkMessageFactory sut = new TriforkMessageFactory(availableWords);

        TriforkMessage message = sut.generate(expectedWordCount);
        int wordCount = message.getContent().split(" ").length;

        assertEquals(expectedWordCount, wordCount);
    }

    @Test
    void generate_OnlyUsesSuppliedAvailableWords()
    {
        String[] availableWords = new String[] { "Lorem", "Ipsum", "Dolor", "Sit", "Amet" };
        TriforkMessageFactory sut = new TriforkMessageFactory(availableWords);
        
        TriforkMessage message = sut.generate(1000);
        String[] usedWords = message.getContent().split(" ");
        availableWords = Arrays.stream(availableWords).sorted().toArray(String[]::new);
        usedWords = Arrays.stream(usedWords).distinct().sorted().toArray(String[]::new);

        assertArrayEquals(availableWords, usedWords);
    }
}
