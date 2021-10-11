package trifork.messagesender;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import trifork.messagesender.application.MessagingService;
import trifork.messagesender.model.TriforkMessage;
import trifork.messagesender.model.TriforkMessageFactory;

@Component
public class Runner  {

    private final MessagingService _service;
    private final TriforkConfiguration _config;

    public Runner(MessagingService service, TriforkConfiguration config) {
        _service = service;
        _config = config;
    }

    @Scheduled(fixedDelay = 1000L)
    public void run() {
        String[] availableWords = _config.getAvailableWords();
        TriforkMessageFactory factory = new TriforkMessageFactory(availableWords);

        String exchange = _config.getExchangeName();
        String routingKey = _config.getRoutingKey() + ".one";

        TriforkMessage message = factory.generate(5);
        _service.sendMessage(message, exchange, routingKey);
    }
    
}
