package org.example.scholarshipmanager.service;

import org.example.scholarshipmanager.model.Scholarship;
import org.example.scholarshipmanager.model.ScholarshipMatch;
import org.example.scholarshipmanager.model.Student;
import org.example.scholarshipmanager.repository.ScholarshipMatchRepository;
import org.example.scholarshipmanager.repository.ScholarshipRepository;
import org.example.scholarshipmanager.repository.StudentRepository;
import org.graph4j.Graph;
import org.graph4j.GraphBuilder;
import org.graph4j.clique.BronKerboschCliqueIterator;
import org.graph4j.util.Clique;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class responsible for finding compatible student groups based on scholarship matching.
 * Uses graph theory algorithms to identify groups of students who are eligible for the same scholarships
 * and can potentially be grouped together for scholarship allocation purposes.
 *
 * <p>The service employs the Bron-Kerbosch algorithm to find maximal cliques in a compatibility graph,
 * where vertices represent students and edges represent compatibility between students.</p>
 *
 * @author Scholarship Manager Team
 * @version 1.0
 * @since 1.0
 */
@Service
public class CompatibleStudentGroupService {

    private final StudentRepository studentRepository;
    private final ScholarshipRepository scholarshipRepository;
    private final ScholarshipMatchRepository matchRepository;

    /**
     * Constructs a new CompatibleStudentGroupService with the required repositories.
     *
     * @param studentRepository repository for student data access
     * @param scholarshipRepository repository for scholarship data access
     * @param matchRepository repository for scholarship match data access
     */
    @Autowired
    public CompatibleStudentGroupService(
            StudentRepository studentRepository,
            ScholarshipRepository scholarshipRepository,
            ScholarshipMatchRepository matchRepository) {
        this.studentRepository = studentRepository;
        this.scholarshipRepository = scholarshipRepository;
        this.matchRepository = matchRepository;
    }

    /**
     * Finds groups of students who are compatible with the same scholarships.
     * Students are considered compatible if they share a minimum number of common scholarships
     * with match scores above the specified threshold.
     *
     * <p>The algorithm works by:
     * <ol>
     *   <li>Filtering scholarship matches by minimum score</li>
     *   <li>Building a compatibility graph where students are vertices</li>
     *   <li>Adding edges between compatible students</li>
     *   <li>Finding maximal cliques using Bron-Kerbosch algorithm</li>
     *   <li>Processing and returning the results</li>
     * </ol>
     * </p>
     *
     * @param minMatchScore minimum match score threshold for considering scholarships (0.0 to 1.0)
     * @param minCommonScholarships minimum number of common scholarships required for compatibility
     * @return list of compatible student groups with their details and common scholarships
     * @throws IllegalArgumentException if parameters are invalid
     */
    public List<Map<String, Object>> findCompatibleStudentGroups(
            double minMatchScore, int minCommonScholarships) {

        List<ScholarshipMatch> matches = matchRepository.findByMatchScoreGreaterThanEqual(
                BigDecimal.valueOf(minMatchScore));

        Map<Integer, Map<Integer, Double>> studentScholarshipScores = createStudentScholarshipMap(matches);

        List<Student> students = studentRepository.findAllById(studentScholarshipScores.keySet());
        Graph graph = buildCompatibilityGraph(students, studentScholarshipScores, minMatchScore, minCommonScholarships);

        List<Clique> cliques = new BronKerboschCliqueIterator(graph).getAll();

        return processCliques(cliques, graph, students, studentScholarshipScores);
    }


    /**
     * Builds a compatibility graph for students based on their shared scholarships.
     * In this graph, each student is represented as a vertex, and edges connect
     * students who are compatible (share enough common scholarships).
     *
     * @param students list of students to include in the graph
     * @param studentScholarshipScores mapping of student IDs to their scholarship scores
     * @param minMatchScore minimum score threshold for considering scholarships
     * @param minCommonScholarships minimum number of common scholarships for compatibility
     * @return the constructed compatibility graph
     */
    private Graph buildCompatibilityGraph(
            List<Student> students,
            Map<Integer, Map<Integer, Double>> studentScholarshipScores,
            double minMatchScore,
            int minCommonScholarships) {

        Graph graph = GraphBuilder.empty().buildGraph();
        Map<Integer, Integer> studentIdToVertex = new HashMap<>();

        for (Student student : students) {
            int vertexId = graph.addVertex();
            studentIdToVertex.put(student.getId(), vertexId);
            graph.setVertexLabel(vertexId, student);
        }

        for (int i = 0; i < students.size(); i++) {
            Student student1 = students.get(i);
            Map<Integer, Double> scholarships1 = studentScholarshipScores.get(student1.getId());

            for (int j = i + 1; j < students.size(); j++) {
                Student student2 = students.get(j);
                Map<Integer, Double> scholarships2 = studentScholarshipScores.get(student2.getId());

                Set<Integer> commonScholarships = findCommonScholarships(
                        scholarships1, scholarships2, minMatchScore);

                if (commonScholarships.size() >= minCommonScholarships) {
                    int v1 = studentIdToVertex.get(student1.getId());
                    int v2 = studentIdToVertex.get(student2.getId());

                    graph.addEdge(v1, v2);
                }
            }
        }

        return graph;
    }

    /**
     * Processes the cliques found by the algorithm and constructs detailed result information.
     * Each clique represents a group of mutually compatible students.
     *
     * @param cliques list of maximal cliques found in the compatibility graph
     * @param graph the compatibility graph used for finding cliques
     * @param students list of all students considered
     * @param studentScholarshipScores mapping of student scholarship scores
     * @return processed list of student groups with detailed information
     */
    private List<Map<String, Object>> processCliques(
            List<Clique> cliques,
            Graph graph,
            List<Student> students,
            Map<Integer, Map<Integer, Double>> studentScholarshipScores) {

        List<Map<String, Object>> results = new ArrayList<>();

        for (int i = 0; i < cliques.size(); i++) {
            Clique clique = cliques.get(i);
            if (clique.size() < 2) continue;

            Map<String, Object> groupResult = new HashMap<>();
            List<Map<String, Object>> studentDetails = new ArrayList<>();
            Set<Integer> studentIds = new HashSet<>();

            for (Integer nodeIdx : clique) {
                Student student = (Student) graph.getVertexLabel(nodeIdx);
                if (student == null) continue;

                studentIds.add(student.getId());
                studentDetails.add(createStudentInfo(student));
            }

            List<Map<String, Object>> commonScholarships =
                    findCommonScholarshipsForGroup(studentIds, studentScholarshipScores);

            groupResult.put("groupId", i + 1);
            groupResult.put("studentCount", clique.size());
            groupResult.put("students", studentDetails);
            groupResult.put("commonScholarships", commonScholarships);
            groupResult.put("commonScholarshipsCount", commonScholarships.size());

            results.add(groupResult);
        }

        results.sort((a, b) -> Integer.compare(
                (Integer) b.get("studentCount"),
                (Integer) a.get("studentCount")));

        return results;
    }


    /**
     * Creates a mapping structure from scholarship matches to organize data by student and scholarship.
     * This creates a nested map where the outer key is student ID and inner key is scholarship ID,
     * with values being the match scores.
     *
     * @param matches list of scholarship matches to process
     * @return nested map structure: studentId -> (scholarshipId -> matchScore)
     */
    private Map<Integer, Map<Integer, Double>> createStudentScholarshipMap(List<ScholarshipMatch> matches) {
        Map<Integer, Map<Integer, Double>> result = new HashMap<>();

        for (ScholarshipMatch match : matches) {
            result
                    .computeIfAbsent(match.getStudent().getId(), k -> new HashMap<>())
                    .put(match.getScholarship().getId(), match.getMatchScore().doubleValue());
        }

        return result;
    }

    /**
     * Finds scholarships that are common between two students and meet the minimum score threshold.
     * Only scholarships where both students have match scores above the threshold are considered common.
     *
     * @param scholarships1 first student's scholarship scores
     * @param scholarships2 second student's scholarship scores
     * @param minMatchScore minimum score threshold for considering scholarships
     * @return set of scholarship IDs that are common to both students
     */
    private Set<Integer> findCommonScholarships(
            Map<Integer, Double> scholarships1,
            Map<Integer, Double> scholarships2,
            double minMatchScore) {

        if (scholarships1 == null || scholarships2 == null) {
            return Collections.emptySet();
        }

        return scholarships1.entrySet().stream()
                .filter(e -> e.getValue() >= minMatchScore)
                .filter(e -> scholarships2.containsKey(e.getKey()) && scholarships2.get(e.getKey()) >= minMatchScore)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }


    /**
     * Creates a summary information map for a student containing essential details.
     * This is used for generating readable output in the API responses.
     *
     * @param student the student to create information for
     * @return map containing student's basic information
     */
    private Map<String, Object> createStudentInfo(Student student) {
        Map<String, Object> info = new HashMap<>();
        info.put("id", student.getId());
        info.put("firstName", student.getFirstName());
        info.put("lastName", student.getLastName());
        info.put("gpa", student.getGpa());
        info.put("yearOfStudy", student.getYearOfStudy());
        info.put("departmentId", student.getDepartment());
        return info;
    }

    /**
     * Finds scholarships that are common to all students in a group.
     * Calculates average match scores for each common scholarship across the group.
     *
     * @param studentIds set of student IDs in the group
     * @param studentScholarshipScores mapping of all student scholarship scores
     * @return list of common scholarships with their details and average scores
     */
    private List<Map<String, Object>> findCommonScholarshipsForGroup(
            Set<Integer> studentIds,
            Map<Integer, Map<Integer, Double>> studentScholarshipScores) {

        Set<Integer> commonScholarshipIds = null;

        for (Integer studentId : studentIds) {
            Map<Integer, Double> scholarships = studentScholarshipScores.get(studentId);

            if (scholarships == null) continue;

            if (commonScholarshipIds == null) {
                commonScholarshipIds = new HashSet<>(scholarships.keySet());
            } else {
                commonScholarshipIds.retainAll(scholarships.keySet());
            }
        }

        if (commonScholarshipIds == null || commonScholarshipIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Integer scholarshipId : commonScholarshipIds) {
            scholarshipRepository.findById(scholarshipId).ifPresent(scholarship -> {
                Map<String, Object> info = new HashMap<>();
                info.put("id", scholarship.getId());
                info.put("name", scholarship.getName());
                info.put("amount", scholarship.getAmount());
                info.put("minGpa", scholarship.getMinGpa());

                double avgScore = studentIds.stream()
                        .map(studentId -> studentScholarshipScores.get(studentId))
                        .filter(Objects::nonNull)
                        .mapToDouble(scores -> scores.getOrDefault(scholarshipId, 0.0))
                        .average()
                        .orElse(0.0);

                info.put("avgMatchScore", avgScore);
                result.add(info);
            });
        }

        result.sort((a, b) -> Double.compare(
                (Double) b.get("avgMatchScore"),
                (Double) a.get("avgMatchScore")));

        return result;
    }
}