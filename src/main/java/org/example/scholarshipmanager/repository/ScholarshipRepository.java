package org.example.scholarshipmanager.repository;

import org.example.scholarshipmanager.model.Scholarship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for CRUD operations and queries
 * on the Scholarship entity.
 */
public interface ScholarshipRepository extends JpaRepository<Scholarship, Integer> {

    /**
     * Finds scholarships by their status.
     *
     * @param status the status of the scholarships to find (e.g., ACTIVE, INACTIVE).
     * @return list of scholarships matching the given status.
     */
    List<Scholarship> findByStatus(Scholarship.ScholarshipStatus status);

    /**
     * Finds all scholarships with status 'ACTIVE'.
     *
     * @return list of scholarships currently active.
     */
    @Query("SELECT s FROM Scholarship s WHERE s.status = 'ACTIVE'")
    List<Scholarship> findAllActiveScholarships();

    /**
     * Counts the number of scholarships with status 'ACTIVE'.
     *
     * @return the count of active scholarships.
     */
    @Query("SELECT COUNT(s) FROM Scholarship s WHERE s.status = 'ACTIVE'")
    int countActiveScholarships();

    /**
     * Finds scholarships by status, ordered by their deadline ascending.
     *
     * @param status the status of the scholarships to find.
     * @return list of scholarships matching the status, sorted by deadline ascending.
     */
    List<Scholarship> findByStatusOrderByDeadlineAsc(Scholarship.ScholarshipStatus status);
}
