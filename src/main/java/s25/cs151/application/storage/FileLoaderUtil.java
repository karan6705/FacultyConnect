package s25.cs151.application.storage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileLoaderUtil {

    /**
     * Loads time slots from a file.
     */
    public static List<String> loadTimeSlotsFromFile(String filename) {
        List<String> items = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) return items;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] chunks = line.split(",");
                if (chunks.length == 2) {
                    String timeSlot = chunks[0].trim() + " - " + chunks[1].trim();
                    items.add(timeSlot);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return items;
    }

    /**
     * Loads courses from a file
     */
    public static List<String> loadCoursesFromFile(String filename) {
        List<String> items = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) return items;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String course = parts[0].trim() + "-" + parts[2].trim();
                    items.add(course);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return items;
    }
}
