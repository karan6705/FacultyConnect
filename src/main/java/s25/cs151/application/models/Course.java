package s25.cs151.application.models;

public class Course {
    private String code;
    private String name;
    private String section;

    public Course(String code, String name, String section) {
        this.code = code;
        this.name = name;
        this.section = section;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getSection() { return section; }

    // Convert a Course to a CSV row.
    public String[] toCSVRow() {
        return new String[]{ code, name, section };
    }

    // Reconstruct a Course from a CSV row.
    public static Course fromCSVRow(String[] row) {
        if (row.length < 3) return null;
        return new Course(row[0], row[1], row[2]);
    }

    @Override
    public String toString() {
        return code + " - " + name + " (Section " + section + ")";
    }
}
