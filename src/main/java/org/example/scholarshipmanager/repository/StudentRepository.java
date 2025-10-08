package org.example.scholarshipmanager.repository;

import org.example.scholarshipmanager.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CRUD operations and queries
 * on the Student entity.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    /**
     * Finds a student by their enrollment number.
     *
     * @param enrollmentNumber the unique enrollment number of the student.
     * @return an Optional containing the found student or empty if none found.
     */
    Optional<Student> findByEnrollmentNumber(String enrollmentNumber);

    /**
     * Finds a student by the associated user's ID.
     *
     * @param userId the ID of the user linked to the student.
     * @return an Optional containing the found student or empty if none found.
     */
    Optional<Student> findByUserId(Integer userId);

    /**
     * Checks if a student exists with the given enrollment number.
     *
     * @param enrollmentNumber the enrollment number to check.
     * @return true if a student with the enrollment number exists, false otherwise.
     */
    boolean existsByEnrollmentNumber(String enrollmentNumber);

    /**
     * Retrieves all students.
     *
     * @return list of all students.
     */
    List<Student> findAll();

    /**
     * Finds a student by the username of their associated user.
     *
     * @param username the username of the linked user.
     * @return the student linked to the given username.
     */
    Student findByUserUsername(String username);
}
