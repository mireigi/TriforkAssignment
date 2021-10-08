package trifork.messagesender;

public class MessagingServiceConfiguration {
    private final String _exchangeName;
    public String getExchangeName() { return _exchangeName; }

    private final String _routingKey;
    public String getRoutingKey() { return _routingKey; }

    private final String _queueName;
    public String getQueueName() { return _queueName; }

    private final String[] _availableWords;
    public String[] getAvailableWords() { return _availableWords; }

    public MessagingServiceConfiguration(String exchangeName, String routingKey, String queueName, String[] availableWords) {
        _exchangeName = exchangeName;
        _routingKey = routingKey;
        _queueName = queueName;
        _availableWords = availableWords;
    }
}
