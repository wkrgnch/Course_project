package com.example.course_project;

public class User {
    private int id;
    private String username;
    private String passwordHash;
    private String role;
    private int teacherId;

    public User(int id, String username, String passwordHash, String role, int teacherId) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.teacherId = teacherId;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
    public int getTeacherId() { return teacherId; }
}