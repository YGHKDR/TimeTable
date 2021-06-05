package com.example.timetable;

import java.io.Serializable;

//可序列化的接口
public class Course implements Serializable {
    private String CourseName;
    private String teacher;
    private String classRoom;
    private int day;
    private int classStart;
    private int classEnd;

    public Course(String courseName, String teacher, String classRoom, int day, int classStart, int classEnd) {
        CourseName = courseName;
        this.teacher = teacher;
        this.classRoom = classRoom;
        this.day = day;
        this.classStart = classStart;
        this.classEnd = classEnd;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getClassStart() {
        return classStart;
    }

    public void setClassStart(int classStart) {
        this.classStart = classStart;
    }

    public int getClassEnd() {
        return classEnd;
    }

    public void setClassEnd(int classEnd) {
        this.classEnd = classEnd;
    }
}
