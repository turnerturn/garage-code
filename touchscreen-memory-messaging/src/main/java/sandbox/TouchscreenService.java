package sandbox;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class TouchscreenService {
    private final ModbusClient modbusClient;

    public TouchscreenService(ModbusClient modbusClient) {
        this.modbusClient = modbusClient;
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
}