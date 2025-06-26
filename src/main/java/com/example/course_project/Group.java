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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }
}

