// File: s25/cs151/application/models/Appointment.java
package s25.cs151.application.models;

public class Appointment {
    private String studentName;
    private String scheduleDate;
    private String timeSlot;
    private String course;

    public Appointment(String studentName, String scheduleDate,
                       String timeSlot, String course) {
        this.studentName = studentName;
        this.scheduleDate = scheduleDate;
        this.timeSlot = timeSlot;
        this.course = course;
    }

    public String getStudentName() { return studentName; }
    public String getScheduleDate() { return scheduleDate; }
    public String getTimeSlot() { return timeSlot; }
    public String getCourse() { return course; }

    public String[] toCSVRow() {
        return new String[]{ studentName, scheduleDate, timeSlot, course, "", ""};
    }

    public static Appointment fromCSVRow(String[] row) {
        if (row.length < 4) return null;

        String studentName = row[0];
        String scheduleDate = row[1];
        String timeSlot = row[2];
        String course = row[3];
        String reason = row.length > 4 ? row[4] : "";
        String comment = row.length > 5 ? row[5] : "";

        if (!reason.isEmpty() || !comment.isEmpty()) {
            return new DetailedAppointment(studentName, scheduleDate, timeSlot, course, reason, comment);
        } else {
            return new Appointment(studentName, scheduleDate, timeSlot, course);
        }
    }

    @Override
    public String toString() {
        return studentName + " - " + scheduleDate + " - " + timeSlot + " - " + course;
    }
}
