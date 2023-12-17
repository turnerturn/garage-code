package sandbox;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
//import IllegalArgumentException;

@Component
public class ModbusClient{
    private String ip;
    private Integer port;
    

    //constructor
    public ModbusClient(){
        System.out.println("ModbusClient constructor");
    }


    public  byte[] convertLongBitsStringToBytes(String value){
        return new byte[4];
    }
    public  byte[] convertBitsStringToBytes(String value){
        return new byte[2];
    }
    public byte[] convertStringToBytes(String value){
        return new byte[20];
    }


    //in display assistant:  always convert our value to bytes before sending to api.
    //this removes challenges on dealing with dint vs long.
    private void convertBytesToRegisters(byte[] bytes) {

    }

    public void writeBytesToRegisters(int offset, byte[] bytes) {
        convertBytesToRegisters( bytes);
        // Write the register value to the Modbus device
        // This is where you'd integrate with your Modbus communication library
        // For example: modbusMaster.writeSingleRegister(registerAddress, registerValue);
    }

}
    /* 
    public  byte[] convertRegistersToBytes(List<Integer> registers) {
        byte[] bytes = new byte[registers.size() * 2];
        int byteIndex = 0;

        for (Integer register : registers) {
            bytes[byteIndex++] = (byte) ((register >> 8) & 0xFF); // High byte
            bytes[byteIndex++] = (byte) (register & 0xFF);       // Low byte
        }

        return bytes;
    }
    *?


    private int convertBytesToRegister(byte[] bytes, int index) {
        // Combine two bytes into one 16-bit register value
        int high = (index < bytes.length) ? (bytes[index] & 0xFF) : 0;
        int low = (index + 1 < bytes.length) ? (bytes[index + 1] & 0xFF) : 0;
        return (high << 8) | low;
    }



    public  String convertLongToBytes(long value) {
        StringBuilder bitString = new StringBuilder();

        for (int i = 15; i >= 0; i--) {
            bitString.append((value >> i) & 1);
        }

        return bitString.toString();
    }
    public  long convertBitsStringToLong(String bitString)throws Exception {
        if (bitString == null || bitString.length() != 16 || !bitString.matches("[01]+")) {
            throw new Exception("Input must be a 16-character string of 0s and 1s.");
        }

        byte[] bytes = new byte[2];
        for (int i = 0; i < 2; i++) {
            String byteString = bitString.substring(i * 8, (i + 1) * 8);
            bytes[i] = (byte) Integer.parseInt(byteString, 2);
        }

        return convertBytesToLong(bytes);
    }
    public long convertBytesToLong(byte[] bytes) {
        if (bytes == null || bytes.length > 8) {
            throw new IllegalArgumentException("Byte array must not be null and must be at most 8 bytes long.");
        }
    
        long value = 0;
        for (int i = 0; i < bytes.length; i++) {
            value |= ((long) bytes[i] & 0xff) << (8 * (bytes.length - i - 1));
        }
    
        return value;
    }
    
*/
