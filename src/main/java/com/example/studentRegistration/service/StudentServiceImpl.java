package com.example.studentRegistration.service;

import com.example.studentRegistration.Repository.StudentRepository;
import com.example.studentRegistration.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    StudentRepository studentRepository;

    // Constructor injection of the StudentRepository
    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Stream<Student> getAllStudents() {return studentRepository.findAll().stream();}

    @Override
    public Student createStudent(Student student) {
        // Business logic for creating a new student (e.g., saving to the database)
        return studentRepository.save(student);
    }
}
