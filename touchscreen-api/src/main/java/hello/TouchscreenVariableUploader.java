package sandbox;

import org.springframework.stereotype.Component;
import org.springframework.jms.annotation.JmsListener;

import org.springframework.messaging.handler.annotation.SendTo;
@Component
public class TouchscreenVariableUploader {
	@SendTo("touchscreen-message-uploads-broadcast")
	@JmsListener(destination = "touchscreen-memory-uploads", containerFactory = "myFactory")
	public TouchscreenMemoryMessage uploadTouchscreenVariable(TouchscreenMemoryMessage touchscreenMemoryMessage) {
		System.out.println("TouchscreenVariableUploader <" + touchscreenMemoryMessage + ">");
		return touchscreenMemoryMessage;
	}

	//This will be used if JmsReplyTo is not set on message received from touchscreen-memory-uploads
	@JmsListener(destination = "touchscreen-message-uploads-broadcast", containerFactory = "myFactory")
	public void broadcastedTouchscreenMessages(TouchscreenMemoryMessage touchscreenMemoryMessage) {
		System.out.println("broadcastedTouchscreenMessages <" + touchscreenMemoryMessage + ">");
		
	}
}
