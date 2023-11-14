import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.input.DataID;
import com.input.DataType;
import com.input.LogEvent;

public class InputTest<T> {
    public static void main(String[] args) {
        File logFile = new File("./com/input/log_file_2.txt");

        // create an arraylist to store lines.
        ArrayList<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            int linesSize = lines.size();
            System.out.println(linesSize);

            for (int i = 0; i < linesSize; i++) {
                // change the line to a LogEvent here
                // 1. separate them by comma
                String toSplit = lines.get(i);
                String[] values = toSplit.split(", ");

                // 2. create Timestamp
                // Define the date format
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                // Parse the timestamp string and create a Date object
                Date parsedDate = dateFormat.parse(values[0]);

                // Create a Timestamp object from the parsed Date
                Timestamp timestamp = new Timestamp(parsedDate.getTime());
//                System.out.println(timestamp);
                // 3. create DataID
                String originalDataID = values[1];
                DataID dataID = new DataID(); // Adjust the package accordingly
                dataID.setName(originalDataID);
                dataID.setType(DataType.STRING); // Adjust the package accordingly
//                System.out.println(dataID.getName());
                // 4. create data Hashmap
                LogEvent<Object> logEvent = new LogEvent<>();
                logEvent.setTimeStamp(timestamp);
                logEvent.setEventType(dataID.getName());
                // Convert the string array to a List<Map<String, Object>>
                String[] dataValuesStringArr = new String[values.length - 2];
                for (int j = 2; j < values.length; j++){
                    dataValuesStringArr[j-2] = values[j];
                }
                List<HashMap<String, Object>> resultMapList = logEvent.stringArrayToHashMapList(dataValuesStringArr);

                // Display the result
                for (Map<String, Object> dataMap : resultMapList) {
                    System.out.println("Value: " + dataMap.get("value") + ", DataType: " + dataMap.get("dataType"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
