package s25.cs151.application.models;

public class Semester {
    private String semester;
    private String year;
    private String days;

    public Semester(String semester, String year, String days) {
        this.semester = semester;
        this.year = year;
        this.days = days;
    }

    public String getSemester() {return semester;}
    public String getYear() {return year;}
    public String getDays() {return days;}

    public String[] toCSVRow() {
        return new String[]{semester, year, days};
    }

    public static Semester fromCSVRow(String[] row) {
        return new Semester(row[0], row[1], row[2]);
    }

}
