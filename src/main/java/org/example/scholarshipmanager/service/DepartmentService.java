package org.example.scholarshipmanager.service;

import org.example.scholarshipmanager.dto.DepartmentDTO;
import org.example.scholarshipmanager.model.Department;
import org.example.scholarshipmanager.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing departments.
 * It handles business logic related to departments and interacts with the repository layer.
 */
@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * Retrieves all departments from the repository and converts them to a list of DepartmentDTO objects.
     *
     * @return a list of {@link DepartmentDTO} containing the ID and name of each department
     */
    public List<DepartmentDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();

        return departments.stream()
                .map(dept -> new DepartmentDTO(dept.getId(), dept.getName()))
                .collect(Collectors.toList());
    }
}
