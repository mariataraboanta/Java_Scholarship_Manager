package org.example.scholarshipmanager.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing a student in the scholarship management system.
 *
 * <p>A Student is linked to a User entity and has academic and extracurricular attributes
 * such as GPA, financial need score, leadership score, and community service hours.</p>
 *
 * <p>Each student belongs to a department and can have multiple scholarship applications.</p>
 */
@Entity
@Table(name = "students")
public class Student {

    /** Unique identifier of the student. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Integer id;

    /** Associated User entity containing authentication and profile info. */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /** Student's first name, cannot be null. */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /** Student's last name, cannot be null. */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /** Unique enrollment number for the student. Only numeric characters are kept. */
    @Column(name = "enrollment_number", nullable = false, unique = true)
    private String enrollmentNumber;

    /** Current year of study (e.g., 1 for first year). */
    @Column(name = "year_of_study")
    private Integer yearOfStudy;

    /** Student's GPA with precision of up to 3 digits and 2 decimal places. */
    @Column(precision = 3, scale = 2)
    private BigDecimal gpa;

    /** Score indicating the student's financial need. */
    @Column(name = "financial_need_score")
    private BigDecimal financialNeedScore;

    /** Number of community service hours completed by the student. */
    @Column(name = "community_service_hours")
    private Integer communityServiceHours;

    /** Score representing the student's leadership qualities. */
    @Column(name = "leadership_score")
    private BigDecimal leadershipScore;

    /** Set of scholarship applications made by the student. */
    @OneToMany(mappedBy = "student")
    private Set<Application> applications = new HashSet<>();

    /** Department to which the student belongs. */
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    /**
     * Returns the unique enrollment number of the student.
     *
     * @return enrollment number with only digits (non-digit characters are removed)
     */
    public String getEnrollmentNumber() {
        return enrollmentNumber;
    }

    /**
     * Sets the enrollment number after removing any non-numeric characters.
     *
     * @param enrollmentNumber the enrollment number string to set
     */
    public void setEnrollmentNumber(String enrollmentNumber) {
        this.enrollmentNumber = enrollmentNumber.replaceAll("[^0-9]", "");
    }

    /**
     * Returns the unique ID of the student.
     *
     * @return student ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique ID of the student.
     *
     * @param id the student ID to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns the first name of the student.
     *
     * @return student's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the student.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the student.
     *
     * @return student's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the student.
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the associated User entity.
     *
     * @return the User object linked to this student
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the associated User entity.
     *
     * @param user the User object to link
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns the current year of study.
     *
     * @return year of study (e.g., 1 for first year)
     */
    public Integer getYearOfStudy() {
        return yearOfStudy;
    }

    /**
     * Sets the current year of study.
     *
     * @param yearOfStudy the year of study to set
     */
    public void setYearOfStudy(Integer yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    /**
     * Returns the GPA of the student.
     *
     * @return student's GPA
     */
    public BigDecimal getGpa() {
        return gpa;
    }

    /**
     * Sets the GPA of the student.
     *
     * @param gpa the GPA value to set
     */
    public void setGpa(BigDecimal gpa) {
        this.gpa = gpa;
    }

    /**
     * Returns the financial need score of the student.
     *
     * @return financial need score
     */
    public BigDecimal getFinancialNeedScore() {
        return financialNeedScore;
    }

    /**
     * Returns the number of community service hours completed.
     *
     * @return community service hours
     */
    public Integer getCommunityServiceHours() {
        return communityServiceHours;
    }

    /**
     * Returns the leadership score of the student.
     *
     * @return leadership score
     */
    public BigDecimal getLeadershipScore() {
        return leadershipScore;
    }

    /**
     * Returns the department the student belongs to.
     *
     * @return student's department
     */
    public Department getDepartment() {
        return department;
    }

    /**
     * Sets the department the student belongs to.
     *
     * @param department the department to set
     */
    public void setDepartment(Department department) {
        this.department = department;
    }
    public void setCommunityServiceHours(Integer communityServiceHours) {
        this.communityServiceHours = communityServiceHours;
    }
    public void setLeadershipScore(BigDecimal leadershipScore) {
        this.leadershipScore = leadershipScore;
    }
    public void setFinancialNeedScore(BigDecimal financialNeedScore) {
        this.financialNeedScore = financialNeedScore;
    }

}
