package org.example.scholarshipmanager.service;

import org.example.scholarshipmanager.model.Scholarship;
import org.example.scholarshipmanager.repository.ScholarshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class that provides information about scholarships.
 * Handles business logic related to scholarships such as counting,
 * retrieving available scholarships, and categorizing scholarship types.
 */
@Service
public class ScholarshipInfoService {

    private final ScholarshipRepository scholarshipRepository;

    /**
     * Constructs a ScholarshipInfoService with the provided ScholarshipRepository.
     *
     * @param scholarshipRepository the repository to access scholarship data
     */
    @Autowired
    public ScholarshipInfoService(ScholarshipRepository scholarshipRepository) {
        this.scholarshipRepository = scholarshipRepository;
    }

    /**
     * Returns the count of active scholarships.
     *
     * @return the number of active scholarships
     */
    public int getScholarshipCount() {
        return (int) scholarshipRepository.countActiveScholarships();
    }

    /**
     * Retrieves a list of available (active) scholarships.
     * Each scholarship is represented as a map of key-value pairs for its properties.
     *
     * @return a list of maps, each containing scholarship details
     */
    public List<Map<String, Object>> getAvailableScholarships() {
        List<Scholarship> activeScholarships = scholarshipRepository.findByStatus(Scholarship.ScholarshipStatus.ACTIVE);

        return activeScholarships.stream()
                .map(this::convertToMap)
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of distinct scholarship type categories based on scholarship names.
     * If no scholarships are found, returns a default list of common scholarship types.
     *
     * @return a list of scholarship type descriptions
     */
    public List<String> getScholarshipTypes() {
        List<String> types = new ArrayList<>();

        Set<String> uniqueTypes = new HashSet<>();

        scholarshipRepository.findAll().forEach(scholarship -> {
            String name = scholarship.getName();
            if (name.contains("Merit")) {
                uniqueTypes.add("Merit-based scholarships");
            } else if (name.contains("Need") || name.contains("Financial")) {
                uniqueTypes.add("Need-based scholarships");
            } else if (name.contains("Sport") || name.contains("Athletic")) {
                uniqueTypes.add("Sports scholarships");
            } else if (name.contains("Research") || name.contains("Academic")) {
                uniqueTypes.add("Research grants");
            } else if (name.contains("International")) {
                uniqueTypes.add("International student scholarships");
            } else {
                uniqueTypes.add("Other scholarships");
            }
        });

        types.addAll(uniqueTypes);

        if (types.isEmpty()) {
            types.add("Merit-based scholarships");
            types.add("Need-based scholarships");
            types.add("Sports scholarships");
            types.add("Research grants");
            types.add("International student scholarships");
        }

        return types;
    }

    /**
     * Converts a Scholarship entity to a map containing key-value pairs of its properties.
     *
     * @param scholarship the scholarship entity to convert
     * @return a map representing the scholarship's data
     */
    private Map<String, Object> convertToMap(Scholarship scholarship) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", scholarship.getId());
        map.put("name", scholarship.getName());
        map.put("description", scholarship.getDescription());
        map.put("amount", scholarship.getAmount());
        map.put("status", scholarship.getStatus().toString());
        map.put("deadline", scholarship.getDeadline());
        map.put("availableSlots", scholarship.getAvailableSlots());
        map.put("minGpa", scholarship.getMinGpa());

        return map;
    }
}
