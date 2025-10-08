package org.example.scholarshipmanager.controller;

import org.example.scholarshipmanager.dto.DepartmentDTO;
import org.example.scholarshipmanager.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsible for handling department-related HTTP requests.
 * Provides endpoints for retrieving department information.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // To allow CORS requests from frontend
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * Retrieves all departments from the system.
     *
     * @return ResponseEntity containing a list of DepartmentDTO objects if successful,
     *         or an error response if an exception occurs
     */
    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentDTO>> getDepartments() {
        try {
            List<DepartmentDTO> departments = departmentService.getAllDepartments();
            return ResponseEntity.ok(departments);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}