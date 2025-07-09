package s25.cs151.application.models;

public class DetailedAppointment extends Appointment {
    private String reason;
    private String comment;

    public DetailedAppointment(String studentName, String scheduleDate, String timeSlot,
                               String course, String reason, String comment) {
        super(studentName, scheduleDate, timeSlot, course);
        this.reason = reason != null ? reason : "";
        this.comment = comment != null ? comment : "";
    }

    public String getReason() { return reason; }
    public String getComment() { return comment; }

    @Override
    public String[] toCSVRow() {
        return new String[] {
                getStudentName(),
                getScheduleDate(),
                getTimeSlot(),
                getCourse(),
                reason != null ? reason : "",
                comment != null ? comment : ""
        };
    }

    @Override
    public String toString() {
        return super.toString() + " Reason: " + reason + ", Comment: " + comment;
    }
}