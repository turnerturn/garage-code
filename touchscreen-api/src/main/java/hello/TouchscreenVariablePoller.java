package sandbox;
import java.util.Map;
import java.util.List;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.jms.annotation.JmsListener;

import org.springframework.messaging.handler.annotation.SendTo;


@Component
public class TouchscreenVariablePoller {
	@SendTo("touchscreen-message-polling-broadcast")
	@JmsListener(destination = "touchscreen-memory-polling", containerFactory = "myFactory")
	public TouchscreenMemoryMessage pollTouchscreenMemory( TouchscreenMemoryMessage touchscreenMemoryMessage) {
		System.out.println("TouchscreenVariablePoller <" + touchscreenMemoryMessage + ">");
		//send downloader message with reply to touchscreen-memory-polling
		Map<String,Object> tags =Optional.ofNullable(touchscreenMemoryMessage.getTags()).orElse(new HashMap<>());
		if(!tags.containsKey("POLLING_VALUES")){
			tags.put("BAD_DATA_ERROR", 1);
			//reply to POLLING_ReplyTo with message 
		}
		//if message.tags.POLLING_START_TIME is set we assume we started polling already
		if(tags.containsKey("POLLING_START_TIME")) {
			List<String> pollingValues = new ArrayList<>();
			Arrays.asList(tags.get("POLLING_VALUES")).forEach(pollingValueObject -> pollingValues.add(String.valueOf(pollingValueObject)));
			if(pollingValues.contains(touchscreenMemoryMessage.getValue())) {
				//reply to POLLING_ReplyTo with message
			} else {
				//if we have timeout. then tag TIMEOUT=true and reply to POLLING_ReplyTo with message.
				LocalDateTime currentLocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault());
				LocalDateTime taggedPollingStartLocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(String.valueOf(tags.get("POLLING_START_TIME")))), ZoneId.systemDefault());
				long timeoutMillis = Long.valueOf(String.valueOf(tags.getOrDefault("POLLING_TIMEOUT_MILLIS", 30 * 1000)));//default to 30 seconds
				if(currentLocalDateTime.isAfter(taggedPollingStartLocalDateTime.plusSeconds(timeoutMillis / 1000))){
					tags.put("TIMEOUT", true);
					//reply to POLLING_ReplyTo with message
				};
				//send message to touchscreen-memory-downloads.  set ReplyTo as touchscreen-memory-polling
			}
		}else{
			tags.put("POLLING_START_TIME", System.currentTimeMillis());
			//send download message.
		}
		return touchscreenMemoryMessage;
	}

	//This will be used if JmsReplyTo is not set on message received from touchscreen-memory-polling
	@JmsListener(destination = "touchscreen-message-polling-broadcast", containerFactory = "myFactory")
	public void broadcastedTouchscreenMessages(TouchscreenMemoryMessage touchscreenMemoryMessage) {
		System.out.println("broadcastedTouchscreenMessages <" + touchscreenMemoryMessage + ">");
	}
}
