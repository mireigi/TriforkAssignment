package trifork.messagesender.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import trifork.common.IMessageQueueGateway;
import trifork.common.core.Result;
import trifork.messagesender.model.TriforkMessage;

@Component
public class MessagingService {
    private static final Logger Log = LoggerFactory.getLogger(MessagingService.class);

    private final IMessageQueueGateway _gateway;
    
    public MessagingService(IMessageQueueGateway gateway) {
        _gateway = gateway;
    }

     public void sendMessage(TriforkMessage message, String exchange, String routingKey)
     {
        Log.info("Sending message '{}'.", message.getContent());
         
         Result result = _gateway.sendMessage(exchange, routingKey, message.getContent());

         if (result.isSuccess()) {
             Log.info("Message successfully sent.");
         }
         else {
             Log.error("Message was not sent. Cause: '{}'", result.getErrorMessage());
         }
     }
}
