package trifork.messagesender;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import trifork.messagesender.application.MessagingService;
import trifork.messagesender.model.TriforkMessage;
import trifork.messagesender.model.TriforkMessageFactory;

@Component
public class Runner implements CommandLineRunner  {

    private final MessagingService _service;

    public Runner(MessagingService service) {
        _service = service;
    }

    @Override
    public void run(String... args) throws Exception {
        String[] availableWords = new String[] { "Lorem", "Ipsum" };
        TriforkMessageFactory factory = new TriforkMessageFactory(availableWords);
        TriforkMessage message = factory.generate(5);
        _service.sendMessage(message, "trifork", "mq.app");
    }
    
}
