package sandbox;

import org.springframework.stereotype.Component;
import org.springframework.jms.annotation.JmsListener;

import org.springframework.messaging.handler.annotation.SendTo;
@Component
public class TouchscreenVariableDownloader {
	//Annotated methods may have a non-void return type. 
	//When they do, the result of the method invocation is sent as a JMS reply to the destination defined by the JMSReplyTO header of the incoming message. 
	//If this header is not set, a default destination can be provided by adding @SendTo to the method declaration.
	@SendTo("touchscreen-memory-downloads-broadcast")
	@JmsListener(destination = "touchscreen-memory-downloads", containerFactory = "myFactory")
	public TouchscreenMemoryMessage downloadTouchscreenMemory(TouchscreenMemoryMessage touchscreenMemoryMessage) {
		System.out.println("TouchscreenVariableDownloader <" + touchscreenMemoryMessage + ">");
		return touchscreenMemoryMessage;
	}

		//This will be used if JmsReplyTo is not set on message received from touchscreen-memory-downloads
		@JmsListener(destination = "touchscreen-message-downloads-broadcast", containerFactory = "myFactory")
		public void broadcastedTouchscreenMessages(TouchscreenMemoryMessage touchscreenMemoryMessage) {
			System.out.println("broadcastedTouchscreenMessages <" + touchscreenMemoryMessage + ">");
			
		}
}
