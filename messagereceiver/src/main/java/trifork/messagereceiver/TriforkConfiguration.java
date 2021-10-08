package trifork.messagereceiver;

public class TriforkConfiguration {
    private String _exchangeName;
    public String getExchangeName() { return _exchangeName; }

    private String _routingKey;
    public String getRoutingKey() { return _routingKey; }

    private String _queueName;
    public String getQueueName() { return _queueName; }

    public TriforkConfiguration(String exchangeName, String routingKey, String queueName) {
        _exchangeName = exchangeName;
        _routingKey = routingKey;
        _queueName = queueName;
    }
}
