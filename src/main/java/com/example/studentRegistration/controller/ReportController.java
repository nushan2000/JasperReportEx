package com.example.studentRegistration.controller;

import com.example.studentRegistration.service.ReportsService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping
public class ReportController {

    @Autowired
    private ReportsService reportsService;



    @GetMapping("/student")
    public ResponseEntity<byte[]> getStudentReport() {
        try {
            byte[] report = reportsService.generateStudentReport();

            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "students_report.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(report);
        } catch (Exception e) {
            e.printStackTrace();  // Log the exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<byte[]> getStudentReportFilter(@PathVariable("id") String studentId,
                                                         @RequestParam("startDate") String startDateString,
                                                         @RequestParam("endDate") String endDateString )
                                                          {
        try {
            Date startDate = Date.valueOf(LocalDate.parse(startDateString));
            Date endDate = Date.valueOf(LocalDate.parse(endDateString));

            byte[] report = reportsService.generateStudentReportFilter(studentId, startDate, endDate);

            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "students_report.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(report);
        } catch (Exception e) {
            e.printStackTrace();  // Log the exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}