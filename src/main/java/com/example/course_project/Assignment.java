package com.example.course_project;


public class Assignment {
    private int teacherId;
    private int courseId;
    private int hours;

    public Assignment(int teacherId, int courseId, int hours) {
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.hours = hours;
    }

    public int getTeacherId() { return teacherId; }
    public int getCourseId() { return courseId; }
    public int getHours() { return hours; }
}
