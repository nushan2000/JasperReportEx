package com.example.studentRegistration.service;

import com.example.studentRegistration.Repository.GradesRepository;
import com.example.studentRegistration.Repository.StudentRepository;
import com.example.studentRegistration.model.Grades;
import com.example.studentRegistration.model.Student;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ReportsService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradesRepository gradesRepository;

    public byte[] generateStudentReport() throws JRException {
        // Load main and subreport
        InputStream mainReportStream = getClass().getResourceAsStream("/reports/Cherry_Landscape_2.jrxml");
        InputStream subReportStream = getClass().getResourceAsStream("/reports/Blank_A4_4.jrxml");

        // Compile main and subreport
        JasperReport mainReport = JasperCompileManager.compileReport(mainReportStream);
        JasperReport subReport = JasperCompileManager.compileReport(subReportStream);

        // Fetch data from the database
        List<Student> students = studentRepository.findAll();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(students);

        // Parameters to pass to the report, including subreport
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("subReportParameter", subReport);
        //parameters.put("studentsDataSource", dataSource);  // Ensure this matches your main report's parameter
        ///parameters.put("gradesDataSource", new JRBeanCollectionDataSource(Collections.singleton(fetchGradesForAllStudents(students))));
        // Fill the report with data

        for (Student student : students) {
            // Pass the current student's ID to the subreport
            parameters.put("student_id", student.getId());

            // You may also set any other required parameters for the main or subreport here
        }

        JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, dataSource);

        // Export the report to PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    private Map<Long, List<Grades>> fetchGradesForAllStudents(List<Student> students) {
        // Fetch grades for each student and group by student ID
        return students.stream()
                .collect(Collectors.toMap(
                        Student::getId,
                        student -> gradesRepository.findByStudentId(student.getId())
                ));
    }
}