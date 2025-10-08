package org.example.scholarshipmanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a scholarship program with eligibility criteria, amount,
 * availability, associated department, and applications.
 *
 * <p>This entity maps to the "scholarships" table in the database.</p>
 */
@Entity
@Table(name = "scholarships")
public class Scholarship {

    /**
     * The unique identifier of the scholarship.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scholarship_id")
    private Integer id;

    /**
     * The name of the scholarship.
     */
    @Column(nullable = false)
    private String name;

    /**
     * A textual description of the scholarship.
     */
    @Column(name = "description")
    private String description;

    /**
     * The monetary amount awarded by the scholarship.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /**
     * The minimum GPA required to apply for this scholarship.
     */
    @Column(name = "min_gpa", precision = 3, scale = 2)
    private BigDecimal minGpa;

    /**
     * The number of available slots for this scholarship.
     */
    @Column(name = "available_slots", nullable = false)
    private Integer availableSlots;

    /**
     * The application deadline for this scholarship.
     */
    @Column(name = "deadline")
    private LocalDate deadline;

    /**
     * The current status of the scholarship (e.g., ACTIVE or CLOSED).
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ScholarshipStatus status;

    /**
     * Weight factor for academic performance in evaluation.
     */
    @Column(name = "academic_weight")
    private BigDecimal academicWeight;

    /**
     * Weight factor for financial need in evaluation.
     */
    @Column(name = "financial_need_weight")
    private BigDecimal financialNeedWeight;

    /**
     * Weight factor for extracurricular activities in evaluation.
     */
    @Column(name = "extracurricular_weight")
    private BigDecimal extracurricularWeight;

    /**
     * The minimum year of study required to apply for this scholarship.
     */
    @Column(name = "min_year_required")
    private Integer minYearRequired;

    /**
     * The department associated with this scholarship.
     */
    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonIgnoreProperties({"scholarships"})
    private Department department;

    /**
     * The set of applications submitted for this scholarship.
     */
    @OneToMany(mappedBy = "scholarship")
    @JsonIgnore
    private Set<Application> applications = new HashSet<>();

    // --- Getters and setters ---

    /**
     * Returns the scholarship ID.
     * @return the scholarship ID.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the scholarship ID.
     * @param id the scholarship ID.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns the name of the scholarship.
     * @return the scholarship name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the scholarship.
     * @param name the scholarship name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the description of the scholarship.
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the scholarship.
     * @param description the description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the scholarship amount.
     * @return the amount.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the scholarship amount.
     * @param amount the amount.
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Returns the minimum GPA required to apply.
     * @return the minimum GPA.
     */
    public BigDecimal getMinGpa() {
        return minGpa;
    }

    /**
     * Sets the minimum GPA required to apply.
     * @param minGpa the minimum GPA.
     */
    public void setMinGpa(BigDecimal minGpa) {
        this.minGpa = minGpa;
    }

    /**
     * Returns the number of available slots.
     * @return available slots.
     */
    public Integer getAvailableSlots() {
        return availableSlots;
    }

    /**
     * Sets the number of available slots.
     * @param availableSlots the available slots.
     */
    public void setAvailableSlots(Integer availableSlots) {
        this.availableSlots = availableSlots;
    }

    /**
     * Returns the application deadline.
     * @return the deadline.
     */
    public LocalDate getDeadline() {
        return deadline;
    }

    /**
     * Sets the application deadline.
     * @param deadline the deadline.
     */
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    /**
     * Returns the scholarship status.
     * @return the status.
     */
    public ScholarshipStatus getStatus() {
        return status;
    }

    /**
     * Sets the scholarship status.
     * @param status the status.
     */
    public void setStatus(ScholarshipStatus status) {
        this.status = status;
    }

    /**
     * Returns the set of applications for this scholarship.
     * @return the applications.
     */
    public Set<Application> getApplications() {
        return applications;
    }

    /**
     * Sets the applications for this scholarship.
     * @param applications the applications.
     */
    public void setApplications(Set<Application> applications) {
        this.applications = applications;
    }

    /**
     * Returns the academic weight for evaluation.
     * @return academic weight.
     */
    public BigDecimal getAcademicWeight() {
        return academicWeight;
    }

    /**
     * Sets the academic weight.
     * @param academicWeight the academic weight.
     */
    public void setAcademicWeight(BigDecimal academicWeight) {
        this.academicWeight = academicWeight;
    }

    /**
     * Returns the financial need weight.
     * @return financial need weight.
     */
    public BigDecimal getFinancialNeedWeight() {
        return financialNeedWeight;
    }

    /**
     * Sets the financial need weight.
     * @param financialNeedWeight the financial need weight.
     */
    public void setFinancialNeedWeight(BigDecimal financialNeedWeight) {
        this.financialNeedWeight = financialNeedWeight;
    }

    /**
     * Returns the extracurricular weight.
     * @return extracurricular weight.
     */
    public BigDecimal getExtracurricularWeight() {
        return extracurricularWeight;
    }

    /**
     * Sets the extracurricular weight.
     * @param extracurricularWeight the extracurricular weight.
     */
    public void setExtracurricularWeight(BigDecimal extracurricularWeight) {
        this.extracurricularWeight = extracurricularWeight;
    }

    /**
     * Returns the minimum year required to apply.
     * @return minimum year required.
     */
    public Integer getMinYearRequired() {
        return minYearRequired;
    }

    /**
     * Sets the minimum year required to apply.
     * @param minYearRequired minimum year required.
     */
    public void setMinYearRequired(Integer minYearRequired) {
        this.minYearRequired = minYearRequired;
    }

    /**
     * Returns the associated department.
     * @return the department.
     */
    public Department getDepartment() {
        return department;
    }

    /**
     * Sets the associated department.
     * @param department the department.
     */
    public void setDepartment(Department department) {
        this.department = department;
    }

    /**
     * Enumeration for scholarship status values.
     */
    public enum ScholarshipStatus {
        /** Scholarship is currently accepting applications. */
        ACTIVE,

        /** Scholarship is closed for applications. */
        CLOSED
    }
}
