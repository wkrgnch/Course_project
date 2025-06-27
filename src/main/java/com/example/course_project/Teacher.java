package com.example.course_project;

public class Teacher {
    private int id;
    private String surname;
    private String name;
    private String patronymic;
    private int experience;
    private String phoneNumber;

    public Teacher(int id, String surname, String name, String patronymic, int experience, String phoneNumber) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.experience = experience;
        this.phoneNumber = phoneNumber;
    }

    public int getId() { return id; }
    public String getSurname() { return surname; }
    public String getName() { return name; }
    public String getPatronymic() { return patronymic; }
    public int getExperience() { return experience; }
    public String getPhoneNumber() { return phoneNumber; }
}
