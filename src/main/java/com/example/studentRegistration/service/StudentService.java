package com.example.studentRegistration.service;

import com.example.studentRegistration.model.Student;

import java.util.List;
import java.util.stream.Stream;

public interface StudentService {
    Student createStudent(Student student);
    Stream<Student> getAllStudents();
}
