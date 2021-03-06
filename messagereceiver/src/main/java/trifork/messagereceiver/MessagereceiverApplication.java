package trifork.messagereceiver;

import javax.naming.ConfigurationException;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import trifork.messagereceiver.data.TriforkDataContext;

@SpringBootApplication
@ComponentScan(basePackages = "trifork.common")
@ComponentScan(basePackages = "trifork.messagereceiver.application")
@ComponentScan(basePackages = "trifork.messagereceiver.data")
public class MessagereceiverApplication {

	@Bean
	TriforkConfiguration config() {
		return new TriforkConfiguration(
			"trifork-exchange",
			"mq.app", 
			"spring-boot"
		 );
	}

	@Bean
	Binding binding(TriforkConfiguration config) {
		String queueName = config.getQueueName();
		String key = config.getRoutingKey() + ".#";
		String exchangeName = config.getExchangeName();

		Queue queue = new Queue(queueName, false);
		TopicExchange exchange = new TopicExchange(exchangeName);

		return BindingBuilder.bind(queue).to(exchange).with(key);
	}

	@Bean
	SimpleMessageListenerContainer container(
        ConnectionFactory connectionFactory,
        TriforkConfiguration config,
        Receiver receiver
    ) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
        container.setMessageListener(receiver);
		
		String queueName = config.getQueueName();
		container.setQueueNames(queueName);
		
		return container;
	}

    @Bean TriforkDataContext dataContext() throws ConfigurationException {
        return TriforkDataContext.init("trifork.db");
    }
    
	public static void main(String[] args) {
		SpringApplication.run(MessagereceiverApplication.class, args);
	}

}
