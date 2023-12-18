package sandbox;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TouchscreenVariableDownloader {

    private final TouchscreenService touchscreenService;

    public TouchscreenVariableDownloader(TouchscreenService touchscreenService) {
        this.touchscreenService = touchscreenService;
    }

	@JmsListener(destination = "touchscreen-variable-downloads", containerFactory = "myFactory")
	public String  downloadTouchscreenVariable( TouchscreenVariableDto variable) throws BadDataException {
		System.out.println("TouchscreenVariableDownloader <" + variable + ">");
		return touchscreenService.readTouchscreenVariables(variable);
	}

}
