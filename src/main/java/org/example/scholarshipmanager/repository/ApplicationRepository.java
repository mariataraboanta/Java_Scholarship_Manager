package org.example.scholarshipmanager.repository;

import org.example.scholarshipmanager.model.Application;
import org.example.scholarshipmanager.model.Student;
import org.example.scholarshipmanager.model.Scholarship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CRUD operations and specific queries
 * on the Application entity.
 *
 * This interface extends JpaRepository and provides custom methods
 * for managing scholarship applications in the scholarship management system.
 */
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    /**
     * Finds all applications made by a specific student.
     *
     * @param student the Student object for which to search applications
     * @return list of all applications for the specified student
     */
    List<Application> findByStudent(Student student);

    /**
     * Finds all applications for a specific scholarship.
     *
     * @param scholarship the Scholarship object for which to search applications
     * @return list of all applications for the specified scholarship
     */
    List<Application> findByScholarship(Scholarship scholarship);

    /**
     * Finds a specific application by student and scholarship.
     *
     * This method is useful for checking if a student has already
     * applied for a particular scholarship.
     *
     * @param student the Student object who made the application
     * @param scholarship the Scholarship object for which the application was made
     * @return Optional containing the application if it exists, or Optional.empty() if not
     */
    Optional<Application> findByStudentAndScholarship(Student student, Scholarship scholarship);

    /**
     * Checks if an application exists for a student and scholarship combination
     * using their IDs.
     *
     * This method is more efficient than loading complete objects
     * when only existence verification is needed.
     *
     * @param studentId the ID of the student
     * @param scholarshipId the ID of the scholarship
     * @return true if an application exists for the specified combination, false otherwise
     */
    boolean existsByStudentIdAndScholarshipId(Integer studentId, Integer scholarshipId);

    /**
     * Finds all applications of a student by their ID.
     *
     * This method is useful when only the student ID is known
     * and loading the complete Student object is not desired.
     *
     * @param studentId the ID of the student
     * @return list of all applications for the specified student
     */
    List<Application> findByStudentId(Integer studentId);

    /**
     * Finds applications filtered by status with pagination support.
     *
     * This method is useful for displaying applications on administration pages,
     * allowing navigation through results using pagination.
     *
     * @param status the application status (e.g., PENDING, APPROVED, REJECTED)
     * @param pageable object containing pagination information
     *                 (current page number, page size, sorting)
     * @return Page object containing applications with the specified status for the current page
     */
    Page<Application> findByStatus(Application.ApplicationStatus status, Pageable pageable);

    /**
     * Counts how many applications have a specific status.
     *
     * This method is useful for displaying statistics
     * and generating administrative reports.
     *
     * @param status the status of applications to count
     * @return total number of applications with the specified status
     */
    long countByStatus(Application.ApplicationStatus status);
}