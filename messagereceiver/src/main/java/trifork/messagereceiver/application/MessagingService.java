package trifork.messagereceiver.application;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import trifork.common.IMessageQueueGateway;
import trifork.common.core.Result;
import trifork.messagereceiver.data.IMessageRepository;
import trifork.messagereceiver.model.TriforkMessage;

@Component
public class MessagingService {
    private static final Logger Log = LoggerFactory.getLogger(MessagingService.class);

    private final IMessageQueueGateway _gateway;
    private final IMessageRepository _messageRepository;

    public MessagingService(IMessageQueueGateway gateway, IMessageRepository messageRepository) {
        super();

        _gateway = gateway;
        _messageRepository = messageRepository;
    }

    public MessageActionEnum determineAction(TriforkMessage message)
    {
        if (message.isOutdated(Instant.now()))
            return MessageActionEnum.Discard;
        
        if (message.isTimestampOdd())
            return MessageActionEnum.Requeue;
        
        return MessageActionEnum.Persist;
    }

    public Result requeueMessage(TriforkMessage message, String exchange, String routingKey) {
        if (determineAction(message) != MessageActionEnum.Requeue)
        {
            Log.warn("The message is not valid for requeuing. Aborting requeue.");
            return Result.Fail("The message is not valid for requeuing.");
        }

        Log.info("Requeueing message '{}'.", message.getContent());
         
        Result gatewayResult = _gateway.sendMessage(exchange, routingKey, message.getContent());

        if (gatewayResult.isSuccess())
            Log.info("Message successfully requeued.");
        else
            Log.error("Message was not requeued. Cause: '{}'.", gatewayResult.getErrorMessage());

        return gatewayResult;
    }

    public Result persistMessage(TriforkMessage message) {
        if (determineAction(message) != MessageActionEnum.Persist)
        {
            Log.warn("The message is not valid for persisting. Aborting persistance.");
            return Result.Fail("The message is not valid for persisting.");
        }

        Result result = _messageRepository.add(message);

        if (result.isSuccess())
            return result;
        
        // TODO: Decide on retry flow
        // Discuss possibilities at job interview
        return result;
    }

}
