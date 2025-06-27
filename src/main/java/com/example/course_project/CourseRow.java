package com.example.course_project;


public class CourseRow {
    private int courseId;
    private String courseName;
    private int groupNumber;
    private String speciality;
    private String direction;

    public CourseRow(int courseId, String courseName, int groupNumber, String speciality, String direction) {
        this.courseId    = courseId;
        this.courseName  = courseName;
        this.groupNumber = groupNumber;
        this.speciality  = speciality;
        this.direction   = direction;
    }

    // Эти геттеры нужны TableView для PropertyValueFactory
    public int getCourseId()     { return courseId; }
    public String getCourseName(){ return courseName; }
    public int getGroupNumber()  { return groupNumber; }
    public String getSpeciality(){ return speciality; }
    public String getDirection(){ return direction; }
}

