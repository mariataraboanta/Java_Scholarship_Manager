package org.example.scholarshipmanager.dto;

/**
 * Data Transfer Object (DTO) for representing a university department.
 * It includes the department ID and department name.
 */
public class DepartmentDTO {

    private Integer departmentId;
    private String name;

    /**
     * Default constructor.
     */
    public DepartmentDTO() {}

    /**
     * Constructs a DepartmentDTO with the specified ID and name.
     *
     * @param departmentId the unique identifier of the department
     * @param name         the name of the department
     */
    public DepartmentDTO(Integer departmentId, String name) {
        this.departmentId = departmentId;
        this.name = name;
    }

    /**
     * Returns the department ID.
     *
     * @return the department ID
     */
    public Integer getDepartmentId() {
        return departmentId;
    }

    /**
     * Sets the department ID.
     *
     * @param departmentId the new department ID
     */
    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * Returns the name of the department.
     *
     * @return the department name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the department.
     *
     * @param name the new department name
     */
    public void setName(String name) {
        this.name = name;
    }
}
