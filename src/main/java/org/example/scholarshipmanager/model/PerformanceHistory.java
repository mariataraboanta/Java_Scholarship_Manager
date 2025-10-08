package org.example.scholarshipmanager.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing historical performance metrics for a department
 * in a given academic year.
 */
@Entity
@Table(name = "performance_history")
public class PerformanceHistory {

    /**
     * Unique identifier for the performance history record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Integer id;

    /**
     * Identifier of the department to which this record belongs.
     */
    @Column(name = "department_id", nullable = false)
    private Integer departmentId;

    /**
     * Academic year for which the performance data is recorded.
     */
    @Column(name = "academic_year", nullable = false)
    private Integer academicYear;

    /**
     * Average GPA of the department for the academic year.
     */
    @Column(name = "avg_gpa", precision = 5, scale = 2)
    private BigDecimal avgGpa;

    /**
     * Graduation rate (percentage) of the department for the academic year.
     */
    @Column(name = "graduation_rate", precision = 5, scale = 2)
    private BigDecimal graduationRate;

    /**
     * Number of research projects completed by the department.
     */
    @Column(name = "research_projects")
    private Integer researchProjects;

    /**
     * Number of industry partnerships the department had.
     */
    @Column(name = "industry_partnerships")
    private Integer industryPartnerships;

    /**
     * Calculated performance score for the department in the academic year.
     * This value is typically computed by a database trigger or external process.
     */
    @Column(name = "performance_score", precision = 5, scale = 2)
    private BigDecimal performanceScore;

    /**
     * Timestamp when this performance record was created.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Default constructor which sets the createdAt field to the current time.
     */
    public PerformanceHistory() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Gets the unique ID of the performance history record.
     * @return the ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique ID of the performance history record.
     * @param id the ID to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the department ID associated with this record.
     * @return the department ID
     */
    public Integer getDepartmentId() {
        return departmentId;
    }

    /**
     * Sets the department ID associated with this record.
     * @param departmentId the department ID to set
     */
    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * Gets the academic year for this record.
     * @return the academic year
     */
    public Integer getAcademicYear() {
        return academicYear;
    }

    /**
     * Sets the academic year for this record.
     * @param academicYear the academic year to set
     */
    public void setAcademicYear(Integer academicYear) {
        this.academicYear = academicYear;
    }

    /**
     * Gets the average GPA of the department.
     * @return the average GPA
     */
    public BigDecimal getAvgGpa() {
        return avgGpa;
    }

    /**
     * Sets the average GPA of the department.
     * @param avgGpa the average GPA to set
     */
    public void setAvgGpa(BigDecimal avgGpa) {
        this.avgGpa = avgGpa;
    }

    /**
     * Gets the graduation rate percentage.
     * @return the graduation rate
     */
    public BigDecimal getGraduationRate() {
        return graduationRate;
    }

    /**
     * Sets the graduation rate percentage.
     * @param graduationRate the graduation rate to set
     */
    public void setGraduationRate(BigDecimal graduationRate) {
        this.graduationRate = graduationRate;
    }

    /**
     * Gets the number of research projects completed.
     * @return the research projects count
     */
    public Integer getResearchProjects() {
        return researchProjects;
    }

    /**
     * Sets the number of research projects completed.
     * @param researchProjects the research projects count to set
     */
    public void setResearchProjects(Integer researchProjects) {
        this.researchProjects = researchProjects;
    }

    /**
     * Gets the number of industry partnerships.
     * @return the industry partnerships count
     */
    public Integer getIndustryPartnerships() {
        return industryPartnerships;
    }

    /**
     * Sets the number of industry partnerships.
     * @param industryPartnerships the industry partnerships count to set
     */
    public void setIndustryPartnerships(Integer industryPartnerships) {
        this.industryPartnerships = industryPartnerships;
    }

    /**
     * Gets the performance score calculated for the department.
     * @return the performance score
     */
    public BigDecimal getPerformanceScore() {
        return performanceScore;
    }

    /**
     * Sets the performance score calculated for the department.
     * @param performanceScore the performance score to set
     */
    public void setPerformanceScore(BigDecimal performanceScore) {
        this.performanceScore = performanceScore;
    }

    /**
     * Gets the timestamp when this record was created.
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the timestamp when this record was created.
     * @param createdAt the creation timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
