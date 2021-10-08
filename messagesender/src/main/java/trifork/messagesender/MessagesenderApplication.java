package trifork.messagesender;

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

@SpringBootApplication
@ComponentScan(basePackages = "trifork.common")
@ComponentScan(basePackages = "trifork.messagesender.application")
public class MessagesenderApplication {

	@Bean
	MessagingServiceConfiguration config() {
		return new MessagingServiceConfiguration(
			"trifork-exchange",
			"mq.app", 
			"spring-boot",
		 	new String[] { "Lorem", "Ipsum", "Dolor", "Sit", "Amet" }
		 );
	}

	@Bean
	Binding binding(MessagingServiceConfiguration config) {
		String queueName = config.getQueueName();
		String key = config.getRoutingKey() + ".#";
		String exchangeName = config.getExchangeName();

		Queue queue = new Queue(queueName, false);
		TopicExchange exchange = new TopicExchange(exchangeName);

		return BindingBuilder.bind(queue).to(exchange).with(key);
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessagingServiceConfiguration config) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		
		String queueName = config.getQueueName();
		container.setQueueNames(queueName);
		
		return container;
	}

	public static void main(String[] args) {
		SpringApplication.run(MessagesenderApplication.class, args);
	}

}
