
package sandbox;

import jakarta.jms.ConnectionFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;


import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;

import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.JmsTemplate;
@SpringBootApplication
@EnableJms
public class Application {


	@Bean
	public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
													DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		// This provides all auto-configured defaults to this factory, including the message converter
		configurer.configure(factory, connectionFactory);
		// You could still override some settings if necessary.
		return factory;
	}

	@Bean // Serialize message content to json using TextMessage
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}

/*
 *     
 *  Map<String,String> map = new HashMap();
 *  map.put("Name", "Mark");
    map.put("Age", new Integer(47));
    jmsTemplate.convertAndSend("testQueue", map, new MessagePostProcessor() {
        public Message postProcessMessage(Message message) throws JMSException {
            message.setIntProperty("AccountID", 1234);
            message.setJMSCorrelationID("123-00001");
            return message;
        }
    });
 */
	public static void main(String[] args) {
		// Launch the application
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		Map<String,Object> tags = new HashMap();
		//TODO this can be stored by sender to track lifecycle of specific meesage.
		String correlationId = "";//UUID.randomUUID().toString();
		tags.put("CORRELATION_ID", correlationId);
		tags.put("VARIABLE_NAME", "VAR_1");
		tags.put("POLLING_TIMEOUT_MILLIS", 60000);
		tags.put("POLLING_VALUES", Arrays.asList("done","abort","page","manual_entry"));
		tags.put("POLLING_ReplyTo", "display-assistant-polling-message");

		// Send a message with a POJO - the template reuse the message converter
		System.out.println("Sending a touchscreen-memory-polling message to upload touchscreen variable.");
		jmsTemplate.convertAndSend("touchscreen-memory-polling", new TouchscreenMemoryMessage("VAR_1","clickclickboom",tags), m -> {

            m.setJMSCorrelationID(correlationId);
            m.setJMSExpiration(60000);//TODO configure this
            //m.setJMSReplyTo(new ActiveMQQueue(ORDER_QUEUE));
            //m.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
           // m.setJMSPriority(Message.DEFAULT_PRIORITY);
            m.setJMSTimestamp(System.nanoTime());
            m.setJMSType("type");
			m.setStringProperty("VARIABLE_NAME", "VAR_1");
			m.setIntProperty("POLLING_TIMEOUT_MILLIS", 60000);
			m.setStringProperty("POLLING_VALUES", "done,abort,page,manual_entry");
			m.setStringProperty("POLLING_ReplyTo", "display-assistant-polling-message");
	
           
            return m;
        });
	}
}
