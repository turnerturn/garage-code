package sandbox;
import java.util.List;
import java.util.Optional;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TouchscreenController {
 
    private final ModbusClient modbusClient;

    public TouchscreenController(ModbusClient modbusClient) {
        this.modbusClient = modbusClient;
    }
    @PostMapping("/api/touchscreen/variables/write")
    public String write(@RequestBody List<TouchscreenVariableDto> variables)
            throws BadDataException {
        for (TouchscreenVariableDto variable : variables) {
            writeTouchscreenVariables(variable);
        }
        return "ok";
    }

    @PostMapping("/api/touchscreen/variables/read")
    public List<TouchscreenVariableDto> read(@RequestBody List<TouchscreenVariableDto> variables)  throws BadDataException {
        for (TouchscreenVariableDto variable : variables) {
            variable.setValue(readTouchscreenVariables(variable));
        }
        return variables;
    }

    // Annotated methods may have a non-void return type.
    // When they do, the result of the method invocation is sent as a JMS reply to
    // the destination defined by the JMSReplyTO header of the incoming message.
    // If this header is not set, a default destination can be provided by adding
    // @SendTo to the method declaration.
    @SendTo("touchscreen-variables-download-broadcast")
    @JmsListener(destination = "touchscreen-variables-download", containerFactory = "myFactory")
    public String readTouchscreenVariables(Message<TouchscreenVariableDto> msg) throws BadDataException {
        return readTouchscreenVariables(msg.getPayload());
    }
     public String readTouchscreenVariables(TouchscreenVariableDto dto) throws BadDataException {
   
        TouchscreenVariable variable = findTouchscreenVariableByName(dto.getName())
                .orElseThrow(() -> new BadDataException("variable not configured for " + dto.getName()));
        if (variable.getIoAddress() == null || variable.getIoAddress().isEmpty()) {
            throw new BadDataException(
                    "ioAddress is not configured for variable.  (variable=" + variable.getName() + ")");
        }
        if (variable.getMaxCount() == null) {
            throw new BadDataException(
                    "maxBits is not configured for variable.  (variable=" + variable.getName() + ")");
        }
        if (variable.getDataType() == null) {
            throw new BadDataException(
                    "dataType is not configured for variable.  (variable=" + variable.getName() + ")");
        }
        int offset = deriveRegisterOffsetFromIoAddress(variable.getIoAddress());

        if ("STRING".equalsIgnoreCase(variable.getDataType())) {
            // TODO is configured count equal to max bits or bytes??
            // 2 characters per register. Make sure we round down when dividing odd number.
            int registerCount = variable.getMaxCount() / 2;
            String value = modbusClient.readStringFromRegisters(offset, registerCount);
            variable.setValue(value);
        } else {
            String value = modbusClient.readBitsFromRegisters(offset, 2);
            variable.setValue(value);
        }
        return variable.getValue();
    }

    // TODO add this to TouchscreenMemory.java 
    //find this from TouchscreenMemory
    private Optional<TouchscreenVariable> findTouchscreenVariableByName(String name) {
        return Optional.ofNullable(new TouchscreenVariable("VAR_1", "", "STRING", "40000.240H", 240));
    }
// TODO add this to TouchscreenMemory.java 
    private int deriveRegisterOffsetFromIoAddress(String ioAddress) {
        return (ioAddress.indexOf(".") > -1) ? Integer.parseInt(ioAddress.substring(0, ioAddress.indexOf(".")))
                : Integer.parseInt(ioAddress);
    }

    @SendTo("touchscreen-variables-upload-broadcast")
    @JmsListener(destination = "touchscreen-variables-upload", containerFactory = "myFactory")
    public String writeTouchscreenVariables( TouchscreenVariableDto dto)
            throws BadDataException {
        TouchscreenVariable variable = findTouchscreenVariableByName(dto.getName())
                .orElseThrow(() -> new BadDataException("variable not configured for " + dto.getName()));
        variable.setValue(dto.getName());

        int offset = deriveRegisterOffsetFromIoAddress(variable.getIoAddress());

        if (variable.getIoAddress() == null || variable.getIoAddress().isEmpty()) {
            throw new BadDataException("ioAddress is not configured for variable.  (variable=" + variable.getName() + ")");
        }
        if (variable.getMaxCount() == null) {
            throw new BadDataException("maxBits is not configured for variable.  (variable=" + variable.getName() + ")");
        }
        if (variable.getDataType() == null) {
            throw new BadDataException("dataType is not configured for variable.  (variable=" + variable.getName() + ")");
        }
        if (variable.getValue() == null) {
            variable.setValue("");
        }
        /*
         * Variable will be STRING data type or DINT data type.
         * - When dealing with dint: we expect to have a string value of 32 bits or 16
         * bits.
         * - string.length of 32 means we have bits representing a Long value.
         * - Else: we handle the first 16 bits of this string as a BitVector. (
         * ----If > 16 and < 32: we ignore the extra bits...
         * ----If < 16: we pad with 0's.
         */
        if ("STRING".equalsIgnoreCase(variable.getDataType())) {
            if (variable.getMaxCount() < variable.getValue().length()) {
                // truncuate variable value from 0 to variable.getMaxCount()
                // this truncuated string ensures we stay within the alloted space of our modbus  registers.
                // refactor with caution. This could unexpectedly change the value of registers outside the scope of this variable.
                variable.setValue(variable.getValue().substring(0, variable.getMaxCount()));
            }
            // pad right with spaces if string is less than maxBits.
            while (variable.getMaxCount() > variable.getValue().length()) {
                variable.setValue(variable.getValue() + " ");
            }
            modbusClient.writeStringToRegisters(offset, variable.getValue());
        } else if (variable.getValue().length() == 32) {
            // we should be dealing with a string value of 32 bits or 16 bits.
            //if != 32 then we will write the first 16 bits from this string to register at offset.
            //Else: then we are likely handling bits for a long value.  We will write to 2 registers starting at offset.
            modbusClient.writeBitsStringToRegisters(offset, variable.getValue());
        }
        return dto.getValue();
    }

    // This will be used if JmsReplyTo is not set on message received from
    // touchscreen-memory-downloads
    @JmsListener(destination = "touchscreen-variables-download-broadcast", containerFactory = "myFactory")
    public void broadcastedTouchscreenDownloadMessages( Message<TouchscreenVariableDto> msg) throws BadDataException {
       System.out.println("broadcastedTouchscreenDownloadMessages("+ msg.getPayload()+")");
    }

    // This will be used if JmsReplyTo is not set on message received from
    // touchscreen-memory-uploads
    @JmsListener(destination = "touchscreen-variables-upload-broadcast", containerFactory = "myFactory")
    public void broadcastedTouchscreenUploadMessages( TouchscreenVariableDto variable) {
        System.out.println("broadcastedTouchscreenUploadMessages("+ variable+")");
    }
}


class TouchscreenVariableDto {
    private String name;
    private String value;
    public TouchscreenVariableDto(){}
    //all args constructor
    public TouchscreenVariableDto(String name, String value) {
        this.name = name;
        this.value = value;
    }
    //getter
	public String getName() {
		return name;
	}
	//setter
	public void setName(String name) {
		this.name = name;
	}
	//getter
	public String getValue() {
		return value;
	}
	//setter
	public void setValue(String value) {
		this.value = value;
	}
    //to string
    public String toString() {
        return "TouchscreenVariableDto(name=" + getName() + ", value=" + getValue() + ")";
    }
}
