package trifork.messagesender;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import trifork.messagesender.application.MessagingService;
import trifork.messagesender.model.TriforkMessage;
import trifork.messagesender.model.TriforkMessageFactory;

@Component
public class Runner implements ApplicationRunner  {

    private final MessagingService _service;
    private final TriforkConfiguration _config;

    public Runner(MessagingService service, TriforkConfiguration config) {
        _service = service;
        _config = config;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String[] availableWords = _config.getAvailableWords();
        TriforkMessageFactory factory = new TriforkMessageFactory(availableWords);

        String exchange = _config.getExchangeName();
        String routingKey = _config.getRoutingKey() + ".one";

        while(true) {
            TriforkMessage message = factory.generate(5);
            _service.sendMessage(message, exchange, routingKey);

            Thread.sleep(1000);
        }
    }
    
}
