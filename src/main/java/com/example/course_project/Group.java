package com.example.course_project;

public class Group {
    private int id;
    private int groupNumber;
    private String speciality;
    private String direction;
    private int studentCount;

    public Group(int id, int groupNumber, String speciality, String direction, int studentCount) {
        this.id = id;
        this.groupNumber = groupNumber;
        this.speciality = speciality;
        this.direction = direction;
        this.studentCount = studentCount;
    }

    public int getId() { return id; }
    public int getGroupNumber() { return groupNumber; }
    public String getSpeciality() { return speciality; }
    public String getDirection() { return direction; }
    public int getStudentCount() { return studentCount; }
}