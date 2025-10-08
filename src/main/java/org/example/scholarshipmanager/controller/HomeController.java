package org.example.scholarshipmanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.scholarshipmanager.service.ScholarshipInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller that handles requests for the home and about pages of the application.
 * Provides information about scholarships and handles user authentication status.
 */
@RestController
@RequestMapping("/api")
public class HomeController {

    private final ScholarshipInfoService scholarshipInfoService;

    /**
     * Constructs a new HomeController with the necessary services.
     *
     * @param scholarshipInfoService Service providing information about scholarships
     */
    @Autowired
    public HomeController(ScholarshipInfoService scholarshipInfoService) {
        this.scholarshipInfoService = scholarshipInfoService;
    }

    /**
     * Handles requests to the home page endpoint.
     * Returns general application information, scholarship counts, available scholarships,
     * and the current user's authentication status.
     *
     * @param request The HTTP request containing user authentication information
     * @return ResponseEntity containing home page data including:
     *         - Application title
     *         - Total scholarship count
     *         - List of available scholarships
     *         - User authentication status and details if logged in
     */
    @GetMapping("/home")
    public ResponseEntity<?> home(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("title", "Scholarship Manager");
        response.put("scholarshipCount", scholarshipInfoService.getScholarshipCount());
        response.put("availableScholarships", scholarshipInfoService.getAvailableScholarships());

        // Get user info from request attributes (set by JWT filter)
        Integer userId = (Integer) request.getAttribute("userId");
        String username = (String) request.getAttribute("username");
        String userRole = (String) request.getAttribute("userRole");

        if (userId != null && username != null && userRole != null) {
            response.put("isLoggedIn", true);
            response.put("username", username);
            response.put("userRole", userRole);
            response.put("userId", userId);
        } else {
            response.put("isLoggedIn", false);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Handles requests to the about page endpoint.
     * Returns information about scholarship types and the current user's authentication status.
     *
     * @param request The HTTP request containing user authentication information
     * @return ResponseEntity containing about page data including:
     *         - Page title
     *         - List of scholarship types
     *         - User authentication status and details if logged in
     */
    @GetMapping("/about")
    public ResponseEntity<?> about(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("title", "About Scholarships");
        response.put("scholarshipTypes", scholarshipInfoService.getScholarshipTypes());

        // Get user info from request attributes (set by JWT filter)
        Integer userId = (Integer) request.getAttribute("userId");
        String username = (String) request.getAttribute("username");
        String userRole = (String) request.getAttribute("userRole");

        if (userId != null && username != null && userRole != null) {
            response.put("isLoggedIn", true);
            response.put("username", username);
            response.put("userRole", userRole);
            response.put("userId", userId);
        } else {
            response.put("isLoggedIn", false);
        }

        return ResponseEntity.ok(response);
    }
}