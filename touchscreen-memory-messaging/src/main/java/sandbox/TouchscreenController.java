package sandbox;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TouchscreenController {
 
    private final TouchscreenService touchscreenService;

    public TouchscreenController(TouchscreenService touchscreenService) {
        this.touchscreenService = touchscreenService;
    }
    @PostMapping("/api/touchscreen/variables/write")
    public String write(@RequestBody List<TouchscreenVariableDto> variables)
            throws BadDataException {
        for (TouchscreenVariableDto variable : variables) {
            touchscreenService.writeTouchscreenVariables(variable);
        }
        return "ok";
    }

    @PostMapping("/api/touchscreen/variables/read")
    public List<TouchscreenVariableDto> read(@RequestBody List<TouchscreenVariableDto> variables)  throws BadDataException {
        for (TouchscreenVariableDto variable : variables) {
            variable.setValue(touchscreenService.readTouchscreenVariables(variable));
        }
        return variables;
    }

}