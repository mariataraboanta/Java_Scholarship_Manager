package org.example.scholarshipmanager.service;

import org.example.scholarshipmanager.model.Student;
import org.example.scholarshipmanager.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for managing operations related to Student entities.
 * Provides methods to create, retrieve, and delete students using the StudentRepository.
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    /**
     * Constructor for StudentService with dependency injection of StudentRepository.
     *
     * @param studentRepository the repository used to interact with Student data.
     */
    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Retrieves all students from the database.
     *
     * @return a list of all students.
     */
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    /**
     * Finds a student by their unique ID.
     *
     * @param id the ID of the student to find.
     * @return an Optional containing the student if found, or empty if not.
     */
    public Optional<Student> findById(Integer id) {
        return studentRepository.findById(id);
    }

    /**
     * Finds a student by their enrollment number.
     *
     * @param enrollmentNumber the enrollment number of the student.
     * @return an Optional containing the student if found, or empty if not.
     */
    public Optional<Student> findByEnrollmentNumber(String enrollmentNumber) {
        return studentRepository.findByEnrollmentNumber(enrollmentNumber);
    }

    /**
     * Checks if a student exists with the given enrollment number.
     *
     * @param enrollmentNumber the enrollment number to check.
     * @return true if a student with the enrollment number exists, false otherwise.
     */
    public boolean existsByEnrollmentNumber(String enrollmentNumber) {
        return studentRepository.existsByEnrollmentNumber(enrollmentNumber);
    }

    /**
     * Finds a student by their associated user ID.
     *
     * @param userId the user ID linked to the student.
     * @return an Optional containing the student if found, or empty if not.
     */
    public Optional<Student> findByUserId(Integer userId) {
        return studentRepository.findByUserId(userId);
    }

    /**
     * Saves or updates a student entity in the database.
     *
     * @param student the student entity to save.
     */
    public void save(Student student) {
        studentRepository.save(student);
    }

    /**
     * Deletes a student from the database by their ID.
     *
     * @param id the ID of the student to delete.
     */
    public void deleteById(Integer id) {
        studentRepository.deleteById(id);
    }
}
