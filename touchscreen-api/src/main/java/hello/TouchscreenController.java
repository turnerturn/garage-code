package sandbox;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.messaging.handler.annotation.SendTo;

@RestController
public class TouchscreenController {
    @Autowired
    private ModbusClient modbusClient;

    @PostMapping("/api/touchscreens/{consoleId}/variables/write")
    public String write(@PathVariable("consoleId") Integer consoleId,
            @RequestBody List<TouchscreenVariable> variables) {

        // TODO write each
        // Check that variable.count is large enough for the number of bytes to write
        // Truncuate bytes to variable count.
        // If string: Fill remainder count with empty space bytes.
        byte[] bytes = null;
        boolean isStringDataType = true;
        int offset;
        int count;
        if (isStringDataType) {
            bytes = modbusClient.convertStringToBytes( variables.get(0).getValue());
        } else {
            if (variables.get(1).getValue().length() == 32) {
                bytes = modbusClient.convertLongBitsStringToBytes( variables.get(1).getValue());

            } else {
                bytes = modbusClient.convertBitsStringToBytes( variables.get(1).getValue());
            }
        }
        modbusClient.writeBytesToRegisters(0, bytes);
        // variable.getMaxBits() < bytes.length){

        // if("STRING" convertStringValueToRegisters(variable.getValue());
        // else
        // if value.length == 32 then convertLongBitsStringToBytes();
        // else convertBitVectorStringToBytes();
        // writeBytesToRegisters();

        // variables.forEach(v -> modbusClient.writeBytesToRegisters(1, v.getValue()));

        return "success";
    }

    @PostMapping("/api/touchscreens/{consoleId}/variables/read")
    public List<TouchscreenVariable> read(@PathVariable("consoleId") Integer consoleId,
            @RequestBody List<TouchscreenVariable> variables) {
        // TODO read each
        // variables.forEach(v -> modbusClient.readBytesFromRegisters(1, v.getValue()));

        return variables;
    }

}