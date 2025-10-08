package org.example.scholarshipmanager.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.scholarshipmanager.dto.DepartmentDTO;
import org.example.scholarshipmanager.dto.ErrorResponseDTO;
import org.example.scholarshipmanager.dto.LoginRequestDTO;
import org.example.scholarshipmanager.dto.RegisterRequestDTO;
import org.example.scholarshipmanager.model.Department;
import org.example.scholarshipmanager.model.Student;
import org.example.scholarshipmanager.model.User;
import org.example.scholarshipmanager.repository.DepartmentRepository;
import org.example.scholarshipmanager.service.CustomUserDetailsService;
import org.example.scholarshipmanager.service.StudentService;
import org.example.scholarshipmanager.service.UserService;
import org.example.scholarshipmanager.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller handling authentication operations such as login, logout, and user registration.
 * Provides endpoints for user authentication and management.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final StudentService studentService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    private DepartmentRepository departmentRepository;
    /**
     * Constructs a new AuthController with the necessary services and utilities.
     *
     * @param userService Service for user management
     * @param studentService Service for student management
     * @param passwordEncoder Encoder for password hashing
     * @param authenticationManager Spring Security authentication manager
     * @param jwtUtil Utility for JWT token operations
     * @param userDetailsService Service for user details retrieval
     */
    @Autowired
    public AuthController(UserService userService,
                          StudentService studentService,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          CustomUserDetailsService userDetailsService) {
        this.userService = userService;
        this.studentService = studentService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Authenticates a user and creates a session.
     * Generates a JWT token and sets it as an HTTP-only cookie.
     *
     * @param loginRequest Contains username and password for authentication
     * @param response HTTP response for setting cookies
     * @return ResponseEntity with login status and user information
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            User user = userDetailsService.findUserByUsername(loginRequest.getUsername());
            String token = jwtUtil.generateToken(loginRequest.getUsername(), user.getRole().name(), user.getId());

            // Set JWT token in cookie
            Cookie jwtCookie = new Cookie("jwt_token", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60);
            response.addCookie(jwtCookie);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.info("User {} logged in successfully with role {}", loginRequest.getUsername(), user.getRole());

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Login successful");
            responseBody.put("role", user.getRole());
            responseBody.put("userId", user.getId());

            return ResponseEntity.ok(responseBody);

        } catch (AuthenticationException e) {
            logger.warn("Failed login attempt for username: {}", loginRequest.getUsername());
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        } catch (Exception e) {
            logger.error("Error during login process: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("error", "An error occurred during login"));
        }
    }

    /**
     * Logs out the current user by invalidating the JWT cookie.
     *
     * @param response HTTP response for clearing cookies
     * @return ResponseEntity with logout status
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Clear JWT cookie
        Cookie jwtCookie = new Cookie("jwt_token", null);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

        SecurityContextHolder.clearContext();

        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

    /**
     * Registers a new user and creates a corresponding student record.
     * Validates that username, email, and enrollment number are unique.
     *
     * @param registerRequest Registration details including user and student information
     * @return ResponseEntity with registration status
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequest) {
        try {
            if (userService.existsByUsername(registerRequest.getUsername())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Username is already taken"));
            }

            if (userService.existsByEmail(registerRequest.getEmail())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email is already taken"));
            }

            if (studentService.existsByEnrollmentNumber(registerRequest.getEnrollmentNumber())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Enrollment number is already taken"));
            }

            Department department = null;
            if (registerRequest.getDepartmentId() != null) {
                Optional<Department> departmentOpt = departmentRepository.findById(registerRequest.getDepartmentId());
                if (departmentOpt.isEmpty()) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid department selected"));
                }
                department = departmentOpt.get();
            }

            if (registerRequest.getFinancialNeedScore() != null &&
                    (registerRequest.getFinancialNeedScore().compareTo(BigDecimal.ZERO) < 0 ||
                            registerRequest.getFinancialNeedScore().compareTo(BigDecimal.valueOf(10.00)) > 0)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Financial need score must be between 0.00 and 10.00"));
            }

            if (registerRequest.getCommunityServiceHours() != null &&
                    registerRequest.getCommunityServiceHours() < 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Community service hours cannot be negative"));
            }

            if (registerRequest.getLeadershipScore() != null &&
                    (registerRequest.getLeadershipScore().compareTo(BigDecimal.ZERO) < 0 ||
                            registerRequest.getLeadershipScore().compareTo(BigDecimal.valueOf(5.00)) > 0)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Leadership score must be between 0.00 and 5.00"));
            }

            // Validate year of study
            if (registerRequest.getYearOfStudy() != null &&
                    (registerRequest.getYearOfStudy() < 1 || registerRequest.getYearOfStudy() > 6)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Year of study must be between 1 and 6"));
            }

            // Validate GPA
            if (registerRequest.getGpa() != null &&
                    (registerRequest.getGpa().compareTo(BigDecimal.ZERO) < 0 ||
                            registerRequest.getGpa().compareTo(BigDecimal.valueOf(10.00)) > 0)) {
                return ResponseEntity.badRequest().body(Map.of("error", "GPA must be between 0.00 and 10.00"));
            }

            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setRole(User.UserRole.STUDENT);
            User savedUser = userService.save(user);

            Student student = new Student();
            student.setFirstName(registerRequest.getFirstName());
            student.setLastName(registerRequest.getLastName());
            student.setEnrollmentNumber(registerRequest.getEnrollmentNumber());
            student.setUser(savedUser);

            if (department != null) {
                student.setDepartment(department);
            }

            student.setFinancialNeedScore(registerRequest.getFinancialNeedScore() != null ?
                    registerRequest.getFinancialNeedScore() : BigDecimal.ZERO);

            student.setCommunityServiceHours(registerRequest.getCommunityServiceHours() != null ?
                    registerRequest.getCommunityServiceHours() : 0);

            student.setLeadershipScore(registerRequest.getLeadershipScore() != null ?
                    registerRequest.getLeadershipScore() : BigDecimal.ZERO);

            student.setYearOfStudy(registerRequest.getYearOfStudy() != null ?
                    registerRequest.getYearOfStudy() : 1);

            student.setGpa(registerRequest.getGpa() != null ?
                    registerRequest.getGpa() : BigDecimal.ZERO);

            studentService.save(student);

            URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/auth/login")
                    .build()
                    .toUri();

            return ResponseEntity.created(location).body(Map.of("message", "Registration successful! You can now login."));

        } catch (Exception e) {
            logger.error("Error during registration: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("error", "An error occurred during registration"));
        }
    }

    @GetMapping("/departments")
    public ResponseEntity<?> getAllDepartments() {
        try {
            List<Department> departmentEntities = departmentRepository.findAll();

            List<DepartmentDTO> departments = departmentEntities.stream()
                    .map(d -> new DepartmentDTO(d.getId(), d.getName()))
                    .toList();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", departments,
                    "message", "Departments fetched successfully"
            ));

        } catch (Exception e) {
            logger.error("Eroare la obținerea departamentelor: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDTO("Eroare de server", "A apărut o eroare la obținerea departamentelor"));
        }
    }
}