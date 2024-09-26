package com.example.studentRegistration.service;

import com.example.studentRegistration.Repository.GradesRepository;
import com.example.studentRegistration.Repository.StudentRepository;
import com.example.studentRegistration.model.Grades;
import com.example.studentRegistration.model.Student;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ReportsGradeService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradesRepository gradesRepository;

    public byte[] generateStudentReport() throws JRException {
        // Load main and subreport

        InputStream subReportStream = getClass().getResourceAsStream("/reports/Blank_A4_4.jrxml");

        // Compile  subreport
        JasperReport subReport = JasperCompileManager.compileReport(subReportStream);

        // Fetch data from the database
        List<Student> students = studentRepository.findAll();
        JRBeanCollectionDataSource studentDataSource = new JRBeanCollectionDataSource(students);


        // Parameters to pass to the report, including subreport
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("student_id", subReport);

        // Set the student data source
        parameters.put("studentsDataSource", studentDataSource);

        JasperPrint jasperPrint = JasperFillManager.fillReport(subReport, parameters, studentDataSource);


        // Combine or export the JasperPrints as needed
        // For simplicity, here we just export the first print (modify as needed)
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }


}