package org.example.scholarshipmanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.scholarshipmanager.dto.*;
import org.example.scholarshipmanager.model.Application;
import org.example.scholarshipmanager.model.Department;
import org.example.scholarshipmanager.repository.ApplicationRepository;
import org.example.scholarshipmanager.repository.DepartmentRepository;
import org.example.scholarshipmanager.repository.ScholarshipRepository;
import org.example.scholarshipmanager.service.CompatibleStudentGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller responsible for administrative operations related to scholarship applications.
 * Provides endpoints for application management, compatible student groups, and application statistics.
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private ApplicationRepository applicationRepository;


    @Autowired
    private CompatibleStudentGroupService groupService;

    /**
     * Finds groups of students who are compatible based on common scholarship eligibility.
     *
     * @return ResponseEntity containing a map with compatible student groups and metadata
     */
    @GetMapping("/compatible")
    public ResponseEntity<Map<String, Object>> findCompatibleStudentGroups() {
        try {
            double minMatchScore = 7.0;
            int minCommonScholarships = 2;
            List<Map<String, Object>> groups = groupService.findCompatibleStudentGroups(
                    minMatchScore, minCommonScholarships);

            Map<String, Object> response = new HashMap<>();
            response.put("totalGroups", groups.size());
            response.put("minMatchScore", minMatchScore);
            response.put("minCommonScholarships", minCommonScholarships);
            response.put("compatibleGroups", groups);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Retrieves a paginated list of applications with optional filtering by status.
     *
     * @param page The page number (zero-based)
     * @param size The page size
     * @param sort The field to sort by
     * @param direction The sort direction (asc or desc)
     * @param status Optional status filter
     * @param request The HTTP request containing authentication information
     * @return ResponseEntity with applications page data or error response
     */
    @GetMapping("/applications")
    public ResponseEntity<?> getAllApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "applicationDate") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) Application.ApplicationStatus status,
            HttpServletRequest request) {

        try {
            Integer userId = (Integer) request.getAttribute("userId");
            String userRole = (String) request.getAttribute("userRole");

            if (userId == null || !"ADMIN".equals(userRole)) {
                logger.warn("Unauthorized access attempt to applications by user: {}", userId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponseDTO("Access denied", "You don't have permissions for this operation"));
            }

            Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

            Page<Application> applications;
            if (status != null) {
                applications = applicationRepository.findByStatus(status, pageable);
            } else {
                applications = applicationRepository.findAll(pageable);
            }

            List<ApplicationResponseDTO> applicationDTOs = applications.getContent().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            ApplicationStatsDTO stats = getApplicationStats();

            ApplicationPageResponseDTO response = new ApplicationPageResponseDTO(
                    applicationDTOs,
                    applications.getTotalElements(),
                    applications.getTotalPages(),
                    page,
                    size,
                    sort,
                    direction,
                    status,
                    stats
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error retrieving applications: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO("Server error", "An error occurred while processing the request"));
        }
    }

    /**
     * Approves a pending scholarship application.
     *
     * @param id The ID of the application to approve
     * @param reviewRequest The review details including optional notes
     * @param request The HTTP request containing authentication information
     * @return ResponseEntity with success or error response
     */
    @PostMapping("/applications/{id}/approve")
    public ResponseEntity<?> approveApplication(
            @PathVariable Integer id,
            @RequestBody ReviewRequestDTO reviewRequest,
            HttpServletRequest request) {

        try {
            Integer userId = (Integer) request.getAttribute("userId");
            String userRole = (String) request.getAttribute("userRole");

            if (userId == null || !"ADMIN".equals(userRole)) {
                logger.warn("Unauthorized approval attempt by user: {}", userId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponseDTO("Access denied", "You don't have permissions for this operation"));
            }

            Optional<Application> applicationOpt = applicationRepository.findById(id);
            if (applicationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponseDTO("Not found", "Application with ID " + id + " does not exist"));
            }

            Application application = applicationOpt.get();

            if (application.getStatus() != Application.ApplicationStatus.PENDING) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponseDTO("Invalid status", "Only pending applications can be approved"));
            }

            application.setStatus(Application.ApplicationStatus.APPROVED);
            if (reviewRequest.getNotes() != null && !reviewRequest.getNotes().trim().isEmpty()) {
                application.setNotes(reviewRequest.getNotes());
            }
            application.setReviewedAt(LocalDateTime.now());
            application.setReviewerId(userId);

            applicationRepository.save(application);

            logger.info("Application {} has been approved by administrator {}", id, userId);

            return ResponseEntity.ok(new SuccessResponseDTO("Application has been approved successfully", convertToDTO(application)));

        } catch (Exception e) {
            logger.error("Error approving application {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO("Server error", "An error occurred while approving the application"));
        }
    }

    /**
     * Rejects a pending scholarship application.
     *
     * @param id The ID of the application to reject
     * @param reviewRequest The review details including mandatory rejection notes
     * @param request The HTTP request containing authentication information
     * @return ResponseEntity with success or error response
     */
    @PostMapping("/applications/{id}/reject")
    public ResponseEntity<?> rejectApplication(
            @PathVariable Integer id,
            @RequestBody ReviewRequestDTO reviewRequest,
            HttpServletRequest request) {

        try {
            Integer userId = (Integer) request.getAttribute("userId");
            String userRole = (String) request.getAttribute("userRole");

            if (userId == null || !"ADMIN".equals(userRole)) {
                logger.warn("Unauthorized rejection attempt by user: {}", userId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponseDTO("Access denied", "You don't have permissions for this operation"));
            }

            Optional<Application> applicationOpt = applicationRepository.findById(id);
            if (applicationOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponseDTO("Not found", "Application with ID " + id + " does not exist"));
            }

            Application application = applicationOpt.get();

            if (application.getStatus() != Application.ApplicationStatus.PENDING) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponseDTO("Invalid status", "Only pending applications can be rejected"));
            }

            if (reviewRequest.getNotes() == null || reviewRequest.getNotes().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponseDTO("Missing notes", "Rejection reason is required"));
            }

            application.setStatus(Application.ApplicationStatus.REJECTED);
            application.setNotes(reviewRequest.getNotes());
            application.setReviewedAt(LocalDateTime.now());
            application.setReviewerId(userId);

            applicationRepository.save(application);

            logger.info("Application {} has been rejected by administrator {}", id, userId);

            return ResponseEntity.ok(new SuccessResponseDTO("Application has been rejected", convertToDTO(application)));

        } catch (Exception e) {
            logger.error("Error rejecting application {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO("Server error", "An error occurred while rejecting the application"));
        }
    }

    /**
     * Retrieves application statistics including counts by status.
     *
     * @param request The HTTP request containing authentication information
     * @return ResponseEntity with application statistics or error response
     */
    @GetMapping("/applications/stats")
    public ResponseEntity<?> getApplicationStatistics(HttpServletRequest request) {
        try {
            Integer userId = (Integer) request.getAttribute("userId");
            String userRole = (String) request.getAttribute("userRole");

            if (userId == null || !"ADMIN".equals(userRole)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponseDTO("Access denied", "You don't have permissions for this operation"));
            }

            ApplicationStatsDTO stats = getApplicationStats();
            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            logger.error("Error retrieving statistics: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO("Server error", "An error occurred while retrieving statistics"));
        }
    }

    /**
     * Converts an Application entity to its DTO representation.
     *
     * @param application The application entity to convert
     * @return The corresponding ApplicationResponseDTO
     */
    private ApplicationResponseDTO convertToDTO(Application application) {
        ApplicationResponseDTO dto = new ApplicationResponseDTO();
        dto.setId(application.getId());
        dto.setApplicationDate(application.getApplicationDate());
        dto.setStatus(application.getStatus().name());
        dto.setMatchScore(application.getMatchScore());
        dto.setNotes(application.getNotes());
        dto.setReviewedAt(application.getReviewedAt());
        dto.setReviewerId(application.getReviewerId());

        if (application.getStudent() != null) {
            dto.setStudentId(application.getStudent().getId());
            dto.setStudentName(application.getStudent().getFirstName() + " " + application.getStudent().getLastName());
            dto.setStudentEnrollmentNumber(application.getStudent().getEnrollmentNumber());
        }

        if (application.getScholarship() != null) {
            dto.setScholarshipId(application.getScholarship().getId());
            dto.setScholarshipName(application.getScholarship().getName());
            dto.setScholarshipAmount(application.getScholarship().getAmount());
        }

        return dto;
    }

    /**
     * Retrieves application statistics by counting applications in each status.
     *
     * @return ApplicationStatsDTO containing counts of applications by status
     */
    private ApplicationStatsDTO getApplicationStats() {
        long pendingCount = applicationRepository.countByStatus(Application.ApplicationStatus.PENDING);
        long approvedCount = applicationRepository.countByStatus(Application.ApplicationStatus.APPROVED);
        long rejectedCount = applicationRepository.countByStatus(Application.ApplicationStatus.REJECTED);
        long totalCount = pendingCount + approvedCount + rejectedCount;

        return new ApplicationStatsDTO(pendingCount, approvedCount, rejectedCount, totalCount);
    }

}