package org.example.scholarshipmanager.service;

import org.example.scholarshipmanager.model.Scholarship;
import org.example.scholarshipmanager.model.ScholarshipMatch;
import org.example.scholarshipmanager.model.Student;
import org.example.scholarshipmanager.repository.ApplicationRepository;
import org.example.scholarshipmanager.repository.ScholarshipMatchRepository;
import org.example.scholarshipmanager.repository.ScholarshipRepository;
import org.example.scholarshipmanager.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing scholarship matches between students and scholarships.
 *
 * <p>This service is responsible for generating compatibility scores between students
 * and scholarships, storing match results, and retrieving top matches for a student.
 * The matching logic considers academic performance, financial need, and extracurricular activities,
 * weighted according to the scholarship's criteria.</p>
 */
@Service
public class ScholarshipMatchService {

    private static final Logger logger = LoggerFactory.getLogger(ScholarshipMatchService.class);

    @Autowired
    private ScholarshipMatchRepository matchRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ScholarshipRepository scholarshipRepository;

    /**
     * Retrieves the top scholarship matches for a specific student.
     *
     * <p>If no matches are found for the student, matches are generated automatically before retrieval.</p>
     *
     * @param studentId the ID of the student for whom to retrieve matches
     * @param limit the maximum number of matches to return
     * @return a list of top {@link ScholarshipMatch} objects sorted by match score descending
     */
    public List<ScholarshipMatch> getTopMatchesForStudent(Integer studentId, int limit) {
        List<ScholarshipMatch> matches = matchRepository.findByStudentIdOrderByMatchScoreDesc(studentId);

        if (matches.isEmpty()) {
            logger.info("No matches found for student {}. Generating matches...", studentId);
            generateMatchesForStudent(studentId);
            matches = matchRepository.findByStudentIdOrderByMatchScoreDesc(studentId);
        }

        List<ScholarshipMatch> topMatches = matches.stream()
                .limit(limit)
                .collect(Collectors.toList());

        for (ScholarshipMatch match : topMatches) {
            boolean hasApplied = applicationRepository.existsByStudentIdAndScholarshipId(
                    studentId, match.getScholarship().getId());
            match.setHasApplication(hasApplied);
        }

        return topMatches;
    }

    /**
     * Generates and persists scholarship matches for all students.
     *
     * <p>This method clears any existing matches and creates new matches based on current student
     * data and scholarship criteria.</p>
     */
    @Transactional
    public void generateMatchesForAllStudents() {
        List<Student> students = studentRepository.findAll();
        List<Scholarship> scholarships = scholarshipRepository.findAll();

        for (Student student : students) {
            generateMatchesForStudent(student, scholarships);
        }
    }

    /**
     * Generates and persists scholarship matches for a specific student by ID.
     *
     * <p>If the student does not exist, the method returns without action.</p>
     *
     * @param studentId the ID of the student
     */
    @Transactional
    public void generateMatchesForStudent(Integer studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            return;
        }

        List<Scholarship> scholarships = scholarshipRepository.findAll();
        generateMatchesForStudent(student, scholarships);
    }

    /**
     * Generates and persists scholarship matches for a specific student.
     *
     * <p>Matches are generated only for scholarships with active status and for students meeting minimum
     * GPA and year of study requirements. Each match's score is computed based on weighted criteria.</p>
     *
     * @param student the {@link Student} object
     * @param scholarships the list of scholarships to consider
     */
    @Transactional
    public void generateMatchesForStudent(Student student, List<Scholarship> scholarships) {
        matchRepository.deleteByStudentId(student.getId());

        for (Scholarship scholarship : scholarships) {
            if (scholarship.getStatus() != Scholarship.ScholarshipStatus.ACTIVE) {
                continue;
            }

            if (student.getGpa() == null || scholarship.getMinGpa() == null ||
                    student.getGpa().compareTo(scholarship.getMinGpa()) < 0) {
                continue;
            }

            if (student.getYearOfStudy() == null || scholarship.getMinYearRequired() == null ||
                    student.getYearOfStudy() < scholarship.getMinYearRequired()) {
                continue;
            }

            double finalScore = calculateMatchScore(student, scholarship);

            logger.debug("Calculated match score for student {} and scholarship {}: {}",
                    student.getId(), scholarship.getId(), finalScore);

            if (finalScore >= 60.0) {
                ScholarshipMatch match = new ScholarshipMatch(
                        student,
                        scholarship,
                        BigDecimal.valueOf(finalScore).setScale(2, java.math.RoundingMode.HALF_UP)
                );
                matchRepository.save(match);
            }
        }
    }

    /**
     * Calculates the overall compatibility score between a student and a scholarship.
     *
     * <p>The score is a weighted sum of academic, financial need, and extracurricular scores,
     * normalized to a 0-100 scale.</p>
     *
     * @param student the {@link Student}
     * @param scholarship the {@link Scholarship}
     * @return the calculated match score as a double between 0 and 100
     */
    private double calculateMatchScore(Student student, Scholarship scholarship) {
        int academicScore = calculateAcademicScore(student, scholarship);
        int financialScore = calculateFinancialScore(student, scholarship);
        int extracurricularScore = calculateExtracurricularScore(student, scholarship);

        BigDecimal academicWeight = scholarship.getAcademicWeight() != null ?
                scholarship.getAcademicWeight() : BigDecimal.valueOf(0.4);
        BigDecimal financialWeight = scholarship.getFinancialNeedWeight() != null ?
                scholarship.getFinancialNeedWeight() : BigDecimal.valueOf(0.3);
        BigDecimal extracurricularWeight = scholarship.getExtracurricularWeight() != null ?
                scholarship.getExtracurricularWeight() : BigDecimal.valueOf(0.3);

        double weightedScore =
                (academicScore * academicWeight.doubleValue()) +
                        (financialScore * financialWeight.doubleValue()) +
                        (extracurricularScore * extracurricularWeight.doubleValue());

        double totalWeight = academicWeight.doubleValue() +
                financialWeight.doubleValue() +
                extracurricularWeight.doubleValue();

        if (totalWeight > 0) {
            return weightedScore / totalWeight;
        }

        return 0.0;
    }

    /**
     * Calculates the academic score for a student relative to a scholarship's minimum GPA.
     *
     * @param student the {@link Student}
     * @param scholarship the {@link Scholarship}
     * @return an integer academic score between 0 and 100
     */
    private int calculateAcademicScore(Student student, Scholarship scholarship) {
        double gpa = student.getGpa().doubleValue();
        double minGpa = scholarship.getMinGpa().doubleValue();
        double maxGpa = 10.0;

        if (gpa >= maxGpa) {
            return 100;
        } else {
            return (int) (((gpa - minGpa) / (maxGpa - minGpa)) * 100);
        }
    }

    /**
     * Calculates the financial need score for a student.
     *
     * @param student the {@link Student}
     * @param scholarship the {@link Scholarship}
     * @return an integer financial need score between 0 and 100
     */
    private int calculateFinancialScore(Student student, Scholarship scholarship) {
        BigDecimal financialWeight = scholarship.getFinancialNeedWeight();
        if (financialWeight == null || financialWeight.compareTo(BigDecimal.ZERO) <= 0) {
            return 50;
        }

        BigDecimal needScore = student.getFinancialNeedScore();
        if (needScore == null) {
            return 0;
        }

        double needValue = needScore.doubleValue();

        if (needValue >= 8) {
            return 100;
        } else if (needValue >= 6) {
            return 80;
        } else if (needValue >= 4) {
            return 60;
        } else if (needValue >= 2) {
            return 40;
        } else {
            return 20;
        }
    }

    /**
     * Calculates the extracurricular score based on community service and leadership.
     *
     * @param student the {@link Student}
     * @param scholarship the {@link Scholarship}
     * @return an integer extracurricular score between 0 and 100
     */
    private int calculateExtracurricularScore(Student student, Scholarship scholarship) {
        BigDecimal extracurricularWeight = scholarship.getExtracurricularWeight();
        if (extracurricularWeight == null || extracurricularWeight.compareTo(BigDecimal.ZERO) <= 0) {
            return 50;
        }

        int volunteerHours = student.getCommunityServiceHours() != null ? student.getCommunityServiceHours() : 0;
        double leadershipPoints = student.getLeadershipScore() != null ? student.getLeadershipScore().doubleValue() : 0.0;

        int volunteerScore = Math.min(50, volunteerHours / 2);
        int leadershipScore = (int) Math.min(50, leadershipPoints * 10);

        return volunteerScore + leadershipScore;
    }
}
