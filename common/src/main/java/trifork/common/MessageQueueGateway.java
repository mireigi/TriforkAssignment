package trifork.common;

import java.time.Instant;
import java.util.Date;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import trifork.common.core.Result;

@Component
public class MessageQueueGateway implements IMessageQueueGateway {

    private final RabbitTemplate _rabbitTemplate;

    public MessageQueueGateway(RabbitTemplate rabbitTemplate) {
        super();

        _rabbitTemplate = rabbitTemplate;
    }

    public Result sendMessage(String exchange, String routingKey, String message)
    {
        try {
            MessagePostProcessor postProcessor = msg -> {
                Date timestamp = Date.from(Instant.now());
                msg.getMessageProperties().setTimestamp(timestamp);
                msg.getMessageProperties().setHeader("timestamp", timestamp);
                return msg;
            };

            _rabbitTemplate.convertAndSend(exchange, routingKey, message, postProcessor);

            return Result.Success();
        }
        catch (Exception e) {
            return Result.Fail("Failed to send message.", e);
        }
    }

}
