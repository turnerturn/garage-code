package sandbox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TouchscreenMemory {
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private ObjectMapper mapper;
/*
    /* 
    @PostConstruct
    protected void init() throws IOException {

        jmsTemplate.convertAndSend("touchscreen-variables-download",
                mapper.writeValueAsBytes(new TouchscreenVariableDto("VAR_1", "")), m -> {

                    m.setJMSCorrelationID("1231");
                    m.setJMSExpiration(60000);// TODO configure this
                    // m.setJMSReplyTo(new ActiveMQQueue(ORDER_QUEUE));
                    // m.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
                    // m.setJMSPriority(Message.DEFAULT_PRIORITY);
                    m.setJMSTimestamp(System.nanoTime());
                    m.setJMSType("type");
                    // m.setJMSReplyTo("touchscreen-variables-download-broadcast");
                    return m;
                });
    }
*/
    // PostConstruct download the csv to json and init in-memory variables
    private String convertCsvToJson(String csvFilePath) throws IOException {
        // JSONArray jsonArray = new JSONArray();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            String[] keys = br.readLine().split(","); // Assuming the first line contains headers

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                // JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < keys.length; i++) {
                    // jsonObject.put(keys[i], values[i]);
                }
                // jsonArray.put(jsonObject);
            }
        }

        return "";// jsonArray.toString();
    }
}