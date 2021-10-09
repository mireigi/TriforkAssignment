package trifork.messagereceiver.model;

import java.time.Instant;

import trifork.common.core.ValueObject;

public class TriforkMessage extends ValueObject {
    private static final int ONE_MINUTE = 60;

    private final String _content;
    public String getContent() { return _content; }

    private final Instant _timestamp;
    public Instant getTimestamp() { return _timestamp; }

    private TriforkMessage(String content, Instant timestamp) {
        super();
        
        _content = content;
        _timestamp = timestamp;
    }

    public Boolean isOutdated(Instant comparisonTimestamp) {
        return comparisonTimestamp.getEpochSecond() - _timestamp.getEpochSecond() > ONE_MINUTE;
    }

    public Boolean isTimestampOdd() {
        return _timestamp.getEpochSecond() % 2 != 0;
    }

    @Override
    protected Object[] getEqualityComponents() {
        return new Object[]{
            _content,
            _timestamp
        };
    }

    public static TriforkMessage create(String content, Instant timestamp)
    {
        if (content == null || content.trim().isEmpty())
            throw new IllegalArgumentException("Content cannot be blank, empty, or null.");
        
        if (timestamp == null)
            throw new IllegalArgumentException("Timestamp cannot be null.");

        return new TriforkMessage(content, timestamp);
    }
}
