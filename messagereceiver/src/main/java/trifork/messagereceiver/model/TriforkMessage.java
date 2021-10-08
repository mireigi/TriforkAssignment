package trifork.messagereceiver.model;

import java.util.Date;

import trifork.common.core.ValueObject;

public class TriforkMessage extends ValueObject {
    private static final int ONE_MINUTE = 60000;

    private String _content;
    public String getContent() { return _content; }

    private Date _timestamp;
    public Date getTimestamp() { return _timestamp; }

    private TriforkMessage(String content, Date timestamp) {
        super();
        
        _content = content;
        _timestamp = timestamp;
    }

    public Boolean isOutdated(Date comparisonDate) {
        return comparisonDate.getTime() - _timestamp.getTime() > ONE_MINUTE;
    }

    public Boolean isTimestampOdd() {
        return _timestamp.getTime() % 2 != 0;
    }

    @Override
    protected Object[] getEqualityComponents() {
        return new Object[]{
            _content,
            _timestamp
        };
    }

    public static TriforkMessage create(String content, Date timestamp)
    {
        if (content == null || content.trim().isEmpty())
            throw new IllegalArgumentException("Content cannot be blank, empty, or null.");
        
        if (timestamp == null)
            throw new IllegalArgumentException("Timestamp cannot be null.");

        return new TriforkMessage(content, timestamp);
    }
}
