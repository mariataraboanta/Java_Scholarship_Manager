package org.example.scholarshipmanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.scholarshipmanager.model.Application;
import org.example.scholarshipmanager.model.ScholarshipMatch;
import org.example.scholarshipmanager.model.Scholarship;
import org.example.scholarshipmanager.model.Student;
import org.example.scholarshipmanager.repository.ApplicationRepository;
import org.example.scholarshipmanager.repository.ScholarshipMatchRepository;
import org.example.scholarshipmanager.repository.ScholarshipRepository;
import org.example.scholarshipmanager.repository.StudentRepository;
import org.example.scholarshipmanager.service.ScholarshipMatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.math.BigDecimal;

/**
 * REST Controller for managing scholarship matches and applications.
 * Provides endpoints for students to view scholarship matches, apply for scholarships,
 * and manage their applications.
 *
 * @author Generated
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/scholarship-matches")
public class ScholarshipMatchController {

    private static final Logger logger = LoggerFactory.getLogger(ScholarshipMatchController.class);

    @Autowired
    private ScholarshipMatchRepository matchRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ScholarshipRepository scholarshipRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ScholarshipMatchService matchService;

    /**
     * Retrieves the top 5 scholarship matches for the authenticated student.
     * Only accessible by users with STUDENT role.
     *
     * @param request HttpServletRequest containing user authentication information
     * @return ResponseEntity containing student information and their top scholarship matches,
     *         or error response if unauthorized or student not found
     *
     * @apiNote GET /api/scholarship-matches
     * @since 1.0
     */
    @GetMapping
    public ResponseEntity<?> getScholarshipMatches(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        String userRole = (String) request.getAttribute("userRole");

        if (userId == null || !"STUDENT".equals(userRole)) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        Optional<Student> studentOptional = studentRepository.findByUserId(userId);
        if (studentOptional.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Student not found"));
        }

        Student student = studentOptional.get();
        logger.info("Finding matches for student with ID: {}", student.getId());

        List<ScholarshipMatch> topMatches = matchService.getTopMatchesForStudent(student.getId(), 5);
        logger.info("Found {} matches for student", topMatches.size());

        Map<String, Object> simpleStudent = new HashMap<>();
        simpleStudent.put("id", student.getId());
        simpleStudent.put("firstName", student.getFirstName());
        simpleStudent.put("lastName", student.getLastName());
        simpleStudent.put("gpa", student.getGpa());
        simpleStudent.put("yearOfStudy", student.getYearOfStudy());

        if (student.getUser() != null) {
            simpleStudent.put("username", student.getUser().getUsername());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("student", simpleStudent);
        response.put("scholarshipMatches", topMatches);
        return ResponseEntity.ok(response);
    }

    /**
     * Processes a scholarship application for the authenticated student.
     * Validates student eligibility, checks for existing applications,
     * and creates a new application if all conditions are met.
     *
     * @param scholarshipId The ID of the scholarship to apply for
     * @param request HttpServletRequest containing user authentication information
     * @return ResponseEntity with success message if application is created successfully,
     *         or error response with specific reason for failure
     *
     * @throws IllegalArgumentException if scholarship ID is invalid
     * @throws IllegalStateException if student has already applied or doesn't meet requirements
     *
     * @apiNote POST /api/scholarship-matches/apply?scholarshipId={id}
     * @since 1.0
     */
    @PostMapping("/apply")
    public ResponseEntity<?> applyForScholarship(@RequestParam("scholarshipId") Integer scholarshipId,
                                                 HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        String userRole = (String) request.getAttribute("userRole");

        if (userId == null || !"STUDENT".equals(userRole)) {
            logger.warn("Attempt to apply without authentication");
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        Optional<Student> studentOptional = studentRepository.findByUserId(userId);
        if (studentOptional.isEmpty()) {
            logger.error("Student not found for userId: {}", userId);
            return ResponseEntity.status(404).body(Map.of("error", "Student not found"));
        }

        Student student = studentOptional.get();

        Optional<Scholarship> scholarshipOptional = scholarshipRepository.findById(scholarshipId);
        if (scholarshipOptional.isEmpty()) {
            logger.warn("Non-existent scholarship with ID: {}", scholarshipId);
            return ResponseEntity.badRequest().body(Map.of("error", "Scholarship not found"));
        }

        Scholarship scholarship = scholarshipOptional.get();

        if (!"ACTIVE".equals(scholarship.getStatus().name())) {
            logger.warn("Attempt to apply to inactive scholarship: {}", scholarshipId);
            return ResponseEntity.badRequest().body(Map.of("error", "Scholarship is no longer available"));
        }

        boolean hasApplied = applicationRepository.existsByStudentIdAndScholarshipId(
                student.getId(), scholarshipId);

        if (hasApplied) {
            logger.info("Student {} tried to apply again to scholarship {}", student.getId(), scholarshipId);
            return ResponseEntity.badRequest().body(Map.of("info", "You have already applied for this scholarship"));
        }

        if (student.getGpa().compareTo(scholarship.getMinGpa()) < 0) {
            logger.info("Student {} doesn't meet GPA requirement for scholarship {}", student.getId(), scholarshipId);
            return ResponseEntity.badRequest().body(Map.of("error",
                    "You don't meet the minimum GPA requirement for this scholarship"));
        }

        if (student.getYearOfStudy() < scholarship.getMinYearRequired()) {
            logger.info("Student {} doesn't meet minimum year requirement for scholarship {}",
                    student.getId(), scholarshipId);
            return ResponseEntity.badRequest().body(Map.of("error",
                    "You don't meet the minimum year of study requirement for this scholarship"));
        }

        try {
            Optional<ScholarshipMatch> matchOptional = matchRepository.findByStudentIdAndScholarshipId(
                    student.getId(), scholarshipId);

            BigDecimal matchScore = matchOptional.isPresent() ?
                    matchOptional.get().getMatchScore() : null;

            Application application = new Application();
            application.setStudent(student);
            application.setScholarship(scholarship);
            application.setApplicationDate(LocalDateTime.now());
            application.setStatus(Application.ApplicationStatus.valueOf("PENDING"));
            application.setMatchScore(matchScore);

            applicationRepository.save(application);
            logger.info("Application created successfully for student {} to scholarship {}",
                    student.getId(), scholarshipId);

            if (scholarship.getAvailableSlots() > 0) {
                scholarship.setAvailableSlots(scholarship.getAvailableSlots() - 1);
                scholarshipRepository.save(scholarship);
                logger.info("Updated slot count for scholarship {}", scholarshipId);
            }

            return ResponseEntity.ok(Map.of("success",
                    "You have successfully applied for the " + scholarship.getName() + " scholarship!"));

        } catch (Exception e) {
            logger.error("Error processing application: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("error",
                    "An error occurred while processing your application: " + e.getMessage()));
        }
    }

    /**
     * Retrieves all applications submitted by the authenticated student.
     * Returns complete application history including application status and details.
     *
     * @param request HttpServletRequest containing user authentication information
     * @return ResponseEntity containing student information and list of their applications,
     *         or error response if unauthorized or student not found
     *
     * @apiNote GET /api/scholarship-matches/my-applications
     * @since 1.0
     */
    @GetMapping("/my-applications")
    public ResponseEntity<?> getMyApplications(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        String userRole = (String) request.getAttribute("userRole");

        if (userId == null || !"STUDENT".equals(userRole)) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        Optional<Student> studentOptional = studentRepository.findByUserId(userId);
        if (studentOptional.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Student not found"));
        }

        Student student = studentOptional.get();

        List<Application> applications = applicationRepository.findByStudentId(student.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("student", student);
        response.put("applications", applications);

        return ResponseEntity.ok(response);
    }
}