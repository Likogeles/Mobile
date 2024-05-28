package com.example.myapplication.Models;

public class Student {
    public String Name;
    public String Surname;
    public int Age;
    public int Course;

    public Student(String name, String surname, int age, int course){
        this.Name = name;
        this.Surname = surname;
        this.Age = age;
        this.Course = course;
    }

    @Override
    public String toString(){
        return String.valueOf(this.Course) + ": " + this.Surname + " " + this.Name + " - " + String.valueOf(this.Age);
    }
}
