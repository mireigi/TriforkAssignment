package trifork.messagesender.model;

import trifork.common.core.ValueObject;

public class TriforkMessage extends ValueObject {
    private final String _content;
    public String getContent() { return _content; }

    private TriforkMessage(String content) {
        super();
        _content = content;
    }

    public static TriforkMessage create(String content)
    {
        if (content == null || content.trim().isEmpty())
            throw new IllegalArgumentException("Content cannot be blank, empty, or null.");

        return new TriforkMessage(content);
    }

    @Override
    protected Object[] getEqualityComponents() {
        return new Object[] {
            _content
        };
    }
}
