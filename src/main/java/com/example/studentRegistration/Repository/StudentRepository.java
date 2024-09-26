package com.example.studentRegistration.Repository;


import com.example.studentRegistration.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // No need to add methods here; JpaRepository provides CRUD methods by default
}