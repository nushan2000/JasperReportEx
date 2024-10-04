package com.example.studentRegistration.service;

import com.example.studentRegistration.Repository.GradesRepository;
import com.example.studentRegistration.Repository.StudentRepository;
import com.example.studentRegistration.model.Grades;
import com.example.studentRegistration.model.Student;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportsService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradesRepository gradesRepository;

    @Autowired
    private DataSource dataSource; // Autowire DataSource to get the connection

    public byte[] generateStudentReport() throws JRException {
        // Load and compile main and subreports
        JasperReport mainReport = JasperCompileManager.compileReport(getResourceStream("/reports/jobsheet2.jrxml"));
        JasperReport subReport = JasperCompileManager.compileReport(getResourceStream("/reports/jobsheet_sub.jrxml"));

        // Fetch data from the database
        List<Student> students = studentRepository.findAll();
        List<Grades> grades = gradesRepository.findAll();

        try (Connection connection = dataSource.getConnection()) {
            // Create a data source for students
            JRBeanCollectionDataSource studentDataSource = new JRBeanCollectionDataSource(students);

            // Create parameters for the main report
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("REPORT_CONNECTION", connection); // Pass the connection for the subreport

            // Combine JasperPrint objects for each student
            List<JasperPrint> jasperPrints = new ArrayList<>();
            for (Student student : students) {
                // Filter grades for the current student
                List<Grades> studentGrades = grades.stream()
                        .filter(grade -> grade.getStudent().getId().equals(student.getId()))
                        .collect(Collectors.toList());

                // Create a data source for the grades subreport
                JRBeanCollectionDataSource gradesDataSource = new JRBeanCollectionDataSource(studentGrades);
                parameters.put("gradesDataSource", gradesDataSource); // Set the grades data source for the subreport

                // Fill the main report with the student data source
                JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, studentDataSource);
                jasperPrints.add(jasperPrint);
            }

            // Export the first JasperPrint as a byte array
            return JasperExportManager.exportReportToPdf(jasperPrints.get(0)); // Export the first JasperPrint to PDF
        } catch (SQLException e) {
            throw new JRException("Failed to get database connection", e);
        }
    }

    public byte[] generateStudentReportFilter(String studentId,Date startDate, Date endDate) throws JRException, SQLException {
        // Load the JasperReport template from resources

        JasperReport mainReport = JasperCompileManager.compileReport(getResourceStream("/reports/jobsheet.jrxml"));
        // Set parameters for the report


        // Set parameters for the report
        Map<String, Object> params = new HashMap<>();
        params.put("shop_id", studentId);
        params.put("Parameter1", studentId); // Use the studentId as a filter
        params.put("startDate", startDate); // Pass start date
        params.put("endDate", endDate);
        // Generate the report with the parameter
        JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, params, dataSource.getConnection());

        // Export the report to a byte array (PDF)
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    private InputStream getResourceStream(String path) {
        return getClass().getResourceAsStream(path);
    }
}
