package trifork.common;

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
            _rabbitTemplate.convertAndSend(exchange, routingKey, message);

            return Result.Success();
        }
        catch (Exception e) {
            return Result.Fail("Failed to send message.", e);
        }
    }

}
