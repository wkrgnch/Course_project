package com.example.course_project;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Assignment {
    private IntegerProperty teacherId = new SimpleIntegerProperty();
    private IntegerProperty courseId = new SimpleIntegerProperty();
    private IntegerProperty hours = new SimpleIntegerProperty();
    public Assignment(int t, int c, int h){
        this.teacherId.set(t);
        this.courseId.set(c);
        this.hours.set(h);
    }
    public IntegerProperty teacherIdProperty(){return teacherId;}
    public IntegerProperty courseIdProperty(){return courseId;}
    public IntegerProperty hoursProperty(){return hours;}
}
