package s25.cs151.application.storage;
import java.io.*;
import java.util.*;

public class Database { // General storage class used for storing data in .txt files
    private final String filePath;
    private final String delimiter;

    public Database(String filePath, String delimiter) {
        this.filePath = filePath;
        this.delimiter = delimiter;
    }

    public void writeData(List<String[]> data) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (String[] row : data) {
                writer.println(String.join(delimiter, row));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> readData() {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line.split(delimiter));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}