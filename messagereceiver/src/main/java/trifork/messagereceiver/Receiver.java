package trifork.messagereceiver;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import trifork.common.core.Result;
import trifork.messagereceiver.application.MessageActionEnum;
import trifork.messagereceiver.application.MessagingService;
import trifork.messagereceiver.model.TriforkMessage;

@Component
public class Receiver implements MessageListener  {
    private static final Logger Log = LoggerFactory.getLogger(Receiver.class);

    private MessagingService _service;
    private TriforkConfiguration _config;

    public Receiver(MessagingService service, TriforkConfiguration config) {
        super();

        _service = service;
        _config = config;
    }

    @Override
    @RabbitListener(queues = "spring-boot")
    public void onMessage(Message receivedMessage) {
        Instant timestamp = receivedMessage.getMessageProperties().getTimestamp().toInstant();
        String content = new String(receivedMessage.getBody());

        Log.info("Message received at '{}'. Content parsed as: '{}'.", timestamp, content);

        TriforkMessage message = TriforkMessage.create(content, timestamp);

        MessageActionEnum action = _service.determineAction(message);

        switch (action) {
            case Requeue:
                handleRequeue(message);
                break;
                
            case Persist:
                handlePersist(message);
                break;

            case Discard:
                handleDiscard(message);
                break;
            
            default:
                Log.error("Enumeration with value '{}' is unhandled.", action);
                throw new AssertionError(String.format("Enumeration with value '%s' is unhandled.", action));
        }
    }

    private void handleDiscard(TriforkMessage message) {
        Log.trace("Message was discarded.");
    }

    private void handleRequeue(TriforkMessage message) {
        String exchange = _config.getExchangeName();
        String routingKey = _config.getRoutingKey();

        Result result = _service.requeueMessage(message, exchange, routingKey);

        if (result.isFailure()) {
            // TODO: Decide on requeue failure flow
            // Discuss possibilities at job interview
        }
    }

    private void handlePersist(TriforkMessage message) {
        Result result = _service.persistMessage(message);

        if (result.isFailure()) {
            // TODO: Decide on persistance failure flow
            // Discuss possibilities at job interview
        }
    }
    
}
