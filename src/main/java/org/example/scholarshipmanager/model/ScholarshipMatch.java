package org.example.scholarshipmanager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing a match between a Student and a Scholarship.
 * It stores information about the match score and the date when the match was created.
 *
 * <p>This entity is used to associate a student with a scholarship they are potentially eligible for,
 * along with a computed matching score.</p>
 *
 * <p>The {@code hasApplication} field is transient and indicates whether the student
 * has already applied for the scholarship.</p>
 *
 * <p>JSON serialization ignores the {@code matches} and {@code applications} properties
 * on {@link Student} and {@link Scholarship} to prevent cyclic references.</p>
 */
@Entity
@Table(name = "scholarship_matches")
public class ScholarshipMatch {

    /** Unique identifier of the scholarship match. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Integer id;

    /** The student involved in the scholarship match. */
    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonIgnoreProperties({"matches", "applications"})
    private Student student;

    /** The scholarship involved in the match. */
    @ManyToOne
    @JoinColumn(name = "scholarship_id")
    @JsonIgnoreProperties({"matches", "applications"})
    private Scholarship scholarship;

    /** The computed score representing how well the student matches the scholarship criteria. */
    @Column(name = "match_score")
    private BigDecimal matchScore;

    /** The date and time when this match was created. */
    @Column(name = "match_date")
    private LocalDateTime matchDate;

    /**
     * Transient flag indicating if the student has already applied to this scholarship.
     * Not persisted in the database.
     */
    @Transient
    private boolean hasApplication;

    /**
     * Default constructor initializing the match date to the current date and time.
     */
    public ScholarshipMatch() {
        this.matchDate = LocalDateTime.now();
    }

    /**
     * Constructs a new ScholarshipMatch with the given student, scholarship, and match score.
     * Initializes the match date to the current date and time.
     *
     * @param student the student involved in the match
     * @param scholarship the scholarship involved in the match
     * @param matchScore the computed match score
     */
    public ScholarshipMatch(Student student, Scholarship scholarship, BigDecimal matchScore) {
        this.student = student;
        this.scholarship = scholarship;
        this.matchScore = matchScore;
        this.matchDate = LocalDateTime.now();
    }

    /** @return the unique identifier of this match */
    public Integer getId() { return id; }

    /** @param id the unique identifier to set */
    public void setId(Integer id) { this.id = id; }

    /** @return the student involved in this match */
    public Student getStudent() { return student; }

    /** @param student the student to set */
    public void setStudent(Student student) { this.student = student; }

    /** @return the scholarship involved in this match */
    public Scholarship getScholarship() { return scholarship; }

    /** @param scholarship the scholarship to set */
    public void setScholarship(Scholarship scholarship) { this.scholarship = scholarship; }

    /** @return the match score */
    public BigDecimal getMatchScore() { return matchScore; }

    /** @param matchScore the match score to set */
    public void setMatchScore(BigDecimal matchScore) { this.matchScore = matchScore; }

    /** @return the date and time the match was created */
    public LocalDateTime getMatchDate() { return matchDate; }

    /** @param matchDate the date and time to set */
    public void setMatchDate(LocalDateTime matchDate) { this.matchDate = matchDate; }

    /** @return {@code true} if the student has applied for the scholarship, {@code false} otherwise */
    public boolean isHasApplication() { return hasApplication; }

    /** @param hasApplication sets whether the student has applied for the scholarship */
    public void setHasApplication(boolean hasApplication) { this.hasApplication = hasApplication; }
}
