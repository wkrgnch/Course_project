package com.example.course_project;

import java.math.BigDecimal;

public class Lesson {
    private int id;
    private int teacherId;
    private int groupId;
    private int courseId;
    private ActivityType activityType;
    private BigDecimal costPerHour;

    public Lesson(int id, int teacherId, int groupId, int courseId,
                  ActivityType activityType, BigDecimal costPerHour) {
        this.id = id;
        this.teacherId = teacherId;
        this.groupId = groupId;
        this.courseId = courseId;
        this.activityType = activityType;
        this.costPerHour = costPerHour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public BigDecimal getCostPerHour() {
        return costPerHour;
    }

    public void setCostPerHour(BigDecimal costPerHour) {
        this.costPerHour = costPerHour;
    }
}

