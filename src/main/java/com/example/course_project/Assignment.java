package com.example.course_project;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class Assignment {
    private int teacherId;
    private int courseId;
    private int hours;


    public Assignment(int teacherId, int courseId, int hours) {
        this.teacherId = teacherId;
        this.courseId  = courseId;
        this.hours     = hours;
    }


    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }
}
//public class Assignment {
//    private IntegerProperty teacherId = new SimpleIntegerProperty();
//    private IntegerProperty courseId = new SimpleIntegerProperty();
//    private IntegerProperty hours = new SimpleIntegerProperty();
//    public Assignment(int t, int c, int h){
//        this.teacherId.set(t);
//        this.courseId.set(c);
//        this.hours.set(h);
//    }
//    public IntegerProperty teacherIdProperty(){return teacherId;}
//    public IntegerProperty courseIdProperty(){return courseId;}
//    public IntegerProperty hoursProperty(){return hours;}
//}
