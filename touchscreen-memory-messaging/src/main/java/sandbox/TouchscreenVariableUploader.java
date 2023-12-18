package sandbox;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TouchscreenVariableUploader {
    private final TouchscreenService touchscreenService;

    public TouchscreenVariableUploader(TouchscreenService touchscreenService) {
        this.touchscreenService = touchscreenService;
    }
	
	@JmsListener(destination = "touchscreen-variable-uploads", containerFactory = "myFactory")
	public String uploadTouchscreenVariable( TouchscreenVariableDto variable) throws BadDataException {
		System.out.println("TouchscreenVariableUploader <" + variable + ">");
		return touchscreenService.writeTouchscreenVariables(variable);
	}

}
