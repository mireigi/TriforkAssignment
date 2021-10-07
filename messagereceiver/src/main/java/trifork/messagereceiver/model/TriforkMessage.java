package trifork.messagereceiver.model;

import java.sql.Timestamp;

public class TriforkMessage {
    private String _content;
    public String getContent() { return _content; }

    private Timestamp _timestamp;
    public Timestamp getTimestamp() { return _timestamp; }

    private TriforkMessage(String content, Timestamp timestamp) {
        super();
        
        _content = content;
        _timestamp = timestamp;
    }

    public static TriforkMessage create(String content, Timestamp timestamp)
    {
        if (content == null || content.trim().isEmpty())
            throw new IllegalArgumentException("Content cannot be blank, empty, or null.");
        
        if (timestamp == null)
            throw new IllegalArgumentException("Timestamp cannot be null.");

        return new TriforkMessage(content, timestamp);
    }
}
