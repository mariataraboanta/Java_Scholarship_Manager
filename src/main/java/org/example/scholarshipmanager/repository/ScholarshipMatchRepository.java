package org.example.scholarshipmanager.repository;

import org.example.scholarshipmanager.model.ScholarshipMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CRUD operations and custom queries
 * on the ScholarshipMatch entity.
 */
@Repository
public interface ScholarshipMatchRepository extends JpaRepository<ScholarshipMatch, Long> {

    /**
     * Deletes all scholarship matches for a given student by their ID.
     *
     * @param studentId the ID of the student whose matches will be deleted.
     */
    void deleteByStudentId(Integer studentId);

    /**
     * Finds all scholarship matches for a given student,
     * ordered by match score in descending order.
     *
     * @param studentId the ID of the student.
     * @return a list of ScholarshipMatch objects sorted by matchScore descending.
     */
    List<ScholarshipMatch> findByStudentIdOrderByMatchScoreDesc(Integer studentId);

    /**
     * Finds a scholarship match for a given student and scholarship.
     *
     * @param studentId the ID of the student.
     * @param scholarshipId the ID of the scholarship.
     * @return an Optional containing the ScholarshipMatch if found, or empty if not.
     */
    Optional<ScholarshipMatch> findByStudentIdAndScholarshipId(Integer studentId, Integer scholarshipId);

    /**
     * Finds scholarship matches with a match score greater than or equal to the specified minimum score.
     *
     * @param minScore the minimum match score (inclusive).
     * @return a list of ScholarshipMatch objects with matchScore >= minScore.
     */
    @Query("SELECT sm FROM ScholarshipMatch sm WHERE sm.matchScore >= :minScore")
    List<ScholarshipMatch> findByMatchScoreGreaterThanEqual(@Param("minScore") BigDecimal minScore);

    /**
     * Finds all scholarship matches for a given scholarship.
     *
     * @param scholarshipId the ID of the scholarship.
     * @return a list of ScholarshipMatch objects for the specified scholarship.
     */
    List<ScholarshipMatch> findByScholarshipId(Integer scholarshipId);
}
