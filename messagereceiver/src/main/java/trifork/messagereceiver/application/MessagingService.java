package trifork.messagereceiver.application;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import trifork.common.IMessageQueueGateway;
import trifork.common.core.Result;
import trifork.messagereceiver.model.TriforkMessage;

public class MessagingService {
    private static final Logger Log = LoggerFactory.getLogger(MessagingService.class);

    private final IMessageQueueGateway _gateway;

    public MessagingService(IMessageQueueGateway gateway) {
        super();

        _gateway = gateway;
    }

    public MessageActionEnum determineAction(TriforkMessage message)
    {
        if (message.isOutdated(new Date()))
            return MessageActionEnum.Discard;
        
        if (message.isTimestampOdd())
            return MessageActionEnum.Requeue;
        
        return MessageActionEnum.Persist;
    }

    public void sendMessage(TriforkMessage message, String exchange, String routingKey)
    {
        Log.info("Sending message '{}'.", message.getContent());
         
         Result result = _gateway.sendMessage(exchange, routingKey, message.getContent());

         if (result.isSuccess())
             Log.info("Message successfully sent.");
         else
             Log.error("Message was not sent. Cause: '{}'.", result.getErrorMessage());
    }

    public void persistMessage(TriforkMessage message) {
        // TODO: Save message to database
    }

}
