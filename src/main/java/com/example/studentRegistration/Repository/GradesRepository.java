package com.example.studentRegistration.Repository;


import com.example.studentRegistration.model.Grades;
import com.example.studentRegistration.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradesRepository extends JpaRepository<Grades, Long> {
    // No need to add methods here; JpaRepository provides CRUD methods by default
    List<Grades> findByStudentId(Long studentId);
}