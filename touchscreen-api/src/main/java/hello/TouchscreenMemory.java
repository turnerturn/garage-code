package sandbox;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

public class TouchscreenMemory{

    //Assistant <-> Touchscreen API.  Messages are passed back and forth until:
    // "DONE" & selectionVariables or ABORT, MANUAL_ENTRY
    // "PAGE": upload messages are sent.
    //If still exchanging: wait a moment and then send download messages for strResponse, currentPage, selectedPurchaseOrder, compartmentLoadMeters ,and compartmentCompartments.
/*
on int:  generate session id. set idle=false.  
    if: sender.command = "ABORT" set session = null.
    on touchscreen-message send:   tag variable messages with SESSION_START_TIME  ignore variables if they dont have the start time of current session.

    send upload messages for page 1 loads. upload currentPage = 1.  send download message for strResponse , currentPage, selectedPurchaseOrder, compartmentLoadMeters ,and compartmentCompartments.
on upload response where strRequest includes "FSCM": send download message for strResponse , currentPage, selectedPurchaseOrder, compartmentLoadMeters ,and compartmentCompartments
on download response where strResponse = "PAGE": -> upload loads for currentPage or page 1 is currentPage is not available. send download message for strResponse , currentPage, selectedPurchaseOrder, compartmentLoadMeters ,and compartmentCompartments
on download response where strResponse = "DONE" ->  if selection variables are not in message: send download message for selectedPurchaseOrder, compartmentLoadMeters ,and compartmentCompartments. Decorate reply message, clear session memory, and reply to spot.
on download response where strResponse = "ABORT" or "MANUAL_ENTRY"->  clear session memory and reply command to spot.

//on any jms listener response when dealing with current session's variable.
if(sessionStartTime is set and current time is > sessionStartTime + timeout){
	clear session start time and send message to spot w/ abort command.
};
*/

    //PostConstruct download the csv to json and init in-memory variables
    private  String convertCsvToJson(String csvFilePath) throws IOException {
    JSONArray jsonArray = new JSONArray();
    try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
        String line;
        String[] keys = br.readLine().split(","); // Assuming the first line contains headers

        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            JSONObject jsonObject = new JSONObject();
            for (int i = 0; i < keys.length; i++) {
                jsonObject.put(keys[i], values[i]);
            }
            jsonArray.put(jsonObject);
        }
    }

    return jsonArray.toString();
}
}