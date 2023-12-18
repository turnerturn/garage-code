package sandbox;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TouchscreenMemory{

    //PostConstruct download the csv to json and init in-memory variables
    private  String convertCsvToJson(String csvFilePath) throws IOException {
   // JSONArray jsonArray = new JSONArray();
    try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
        String line;
        String[] keys = br.readLine().split(","); // Assuming the first line contains headers

        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
         //   JSONObject jsonObject = new JSONObject();
            for (int i = 0; i < keys.length; i++) {
            //    jsonObject.put(keys[i], values[i]);
            }
         //   jsonArray.put(jsonObject);
        }
    }

    return"";//jsonArray.toString();
}
}