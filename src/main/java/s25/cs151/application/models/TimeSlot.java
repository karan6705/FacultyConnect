package s25.cs151.application.models;

public class TimeSlot {
    private final int fromHour;
    private final int fromMinute;
    private final int toHour;
    private final int toMinute;

    // Updated constructor: order is (fromHour, fromMinute, toHour, toMinute)
    public TimeSlot(int fromHour, int fromMinute, int toHour, int toMinute) {
        this.fromHour = fromHour;
        this.fromMinute = fromMinute;
        this.toHour = toHour;
        this.toMinute = toMinute;
    }

    // Getter for formatted start time
    public String getFromTime() {
        return String.format("%02d:%02d", fromHour, fromMinute);
    }

    // Getter for formatted end time
    public String getToTime() {
        return String.format("%02d:%02d", toHour, toMinute);
    }

    // Used for sorting by start time
    public String getSortKey() {
        return String.format("%02d:%02d", fromHour, fromMinute);
    }
}
