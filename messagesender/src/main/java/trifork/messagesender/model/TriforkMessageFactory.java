package trifork.messagesender.model;

import java.util.Random;

public class TriforkMessageFactory {
    private String[] _availableWords = new String[] {
        "Lorem", "Ipsum", "Dolor", "Sit", "Amet",
        "Hello", "World", "One", "Two", "Three",
        "Buzzword", "Language", "Github", "Java"
    };

    private Random _randomizer;

    public TriforkMessageFactory(String[] availableWords) {
        super();

        if (availableWords == null || availableWords.length == 0)
            throw new IllegalArgumentException("The array of available words cannot be empty or null.");

        _availableWords = availableWords;
        _randomizer = new Random();
    }

    public TriforkMessage generate(int wordCount) {
        if (wordCount < 1)
            throw new IllegalArgumentException("Minimum word count is 1.");

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < wordCount; i++) {
            if (i > 0)
                builder.append(' ');
            
            int wordIndex = _randomizer.nextInt(_availableWords.length);
            builder.append(_availableWords[wordIndex]);
        }

        return TriforkMessage.Create(builder.toString());
    }
}
