package com.advpro.profiling.tutorial.service;

import com.advpro.profiling.tutorial.model.Student;
import com.advpro.profiling.tutorial.model.StudentCourse;
import com.advpro.profiling.tutorial.repository.StudentCourseRepository;
import com.advpro.profiling.tutorial.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author muhammad.khadafi
 */
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    public List<StudentCourse> getAllStudentsWithCourses() {
        // Menggunakan query yang dioptimalkan dari repository
        return studentCourseRepository.findAllStudentsWithCourses();
    }

    public Optional<Student> findStudentWithHighestGpa() {
        // Menggunakan query yang dioptimalkan dari repository
        return studentRepository.findStudentWithHighestGpa();
    }

    public String joinStudentNames() {
        // Jika query di repository tidak berfungsi, gunakan implementasi dengan StringBuilder
        List<Student> students = studentRepository.findAll();
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < students.size(); i++) {
            result.append(students.get(i).getName());
            if (i < students.size() - 1) {
                result.append(", ");
            }
        }
        
        return result.toString();
    }
    
    // Jika STRING_AGG tidak didukung oleh database Anda, gunakan implementasi dengan StringBuilder:
    public String joinStudentNamesWithStringBuilder() {
        List<Student> students = studentRepository.findAll();
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < students.size(); i++) {
            result.append(students.get(i).getName());
            if (i < students.size() - 1) {
                result.append(", ");
            }
        }
        
        return result.toString();
    }
}

