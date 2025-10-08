package org.example.scholarshipmanager.model;

import jakarta.persistence.*;

/**
 * Entity representing a department within the university or institution.
 */
@Entity
@Table(name = "departments")
public class Department {

    /**
     * Unique identifier for the department.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Integer id;

    /**
     * Name of the department.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Gets the unique identifier of the department.
     * @return the department ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the department.
     * @param id the department ID to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the name of the department.
     * @return the department name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the department.
     * @param name the department name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
