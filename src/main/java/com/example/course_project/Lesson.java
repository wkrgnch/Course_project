package com.example.course_project;

import java.math.BigDecimal;

public class Lesson {
    private int id;
    private int teacherId;
    private int groupId;
    private int courseId;
    private ActivityType activityType;
    private BigDecimal costPerHour;

    public Lesson(int id, int teacherId, int groupId, int courseId, String activityType, BigDecimal costPerHour) {
        this.id = id;
        this.teacherId = teacherId;
        this.groupId = groupId;
        this.courseId = courseId;
        this.activityType = ActivityType.valueOf(activityType.toString());
        this.costPerHour = costPerHour;
    }

    public int getId() { return id; }
    public int getTeacherId() { return teacherId; }
    public int getGroupId() { return groupId; }
    public int getCourseId() { return courseId; }
    public ActivityType getActivityType() { return activityType; }
    public BigDecimal getCostPerHour() { return costPerHour; }
}