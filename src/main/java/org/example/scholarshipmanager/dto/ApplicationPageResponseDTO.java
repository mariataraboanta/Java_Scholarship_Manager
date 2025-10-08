package org.example.scholarshipmanager.dto;

import org.example.scholarshipmanager.model.Application;

import java.util.List;

/**
 * Data Transfer Object for paginated application results.
 * Contains application data along with pagination information and statistics.
 */
public class ApplicationPageResponseDTO {
    private List<ApplicationResponseDTO> applications;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int size;
    private String sort;
    private String direction;
    private Application.ApplicationStatus currentStatus;
    private ApplicationStatsDTO stats;

    /**
     * Constructs a new ApplicationPageResponse with all required fields.
     *
     * @param applications List of application DTOs for the current page
     * @param totalElements Total number of elements across all pages
     * @param totalPages Total number of pages
     * @param currentPage Current page number (zero-based)
     * @param size Page size
     * @param sort Sort field
     * @param direction Sort direction
     * @param currentStatus Current status filter (if any)
     * @param stats Application statistics
     */
    public ApplicationPageResponseDTO(List<ApplicationResponseDTO> applications, long totalElements,
                                      int totalPages, int currentPage, int size, String sort,
                                      String direction, Application.ApplicationStatus currentStatus,
                                      ApplicationStatsDTO stats) {
        this.applications = applications;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.size = size;
        this.sort = sort;
        this.direction = direction;
        this.currentStatus = currentStatus;
        this.stats = stats;
    }

    /**
     * Gets the list of applications for the current page.
     *
     * @return List of application DTOs
     */
    public List<ApplicationResponseDTO> getApplications() {
        return applications;
    }

    /**
     * Sets the list of applications for the current page.
     *
     * @param applications List of application DTOs
     */
    public void setApplications(List<ApplicationResponseDTO> applications) {
        this.applications = applications;
    }

    /**
     * Gets the total number of elements across all pages.
     *
     * @return Total element count
     */
    public long getTotalElements() {
        return totalElements;
    }

    /**
     * Sets the total number of elements across all pages.
     *
     * @param totalElements Total element count
     */
    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    /**
     * Gets the total number of pages.
     *
     * @return Total page count
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * Sets the total number of pages.
     *
     * @param totalPages Total page count
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * Gets the current page number (zero-based).
     *
     * @return Current page number
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * Sets the current page number.
     *
     * @param currentPage Current page number
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * Gets the page size.
     *
     * @return Page size
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the page size.
     *
     * @param size Page size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Gets the sort field.
     *
     * @return Sort field
     */
    public String getSort() {
        return sort;
    }

    /**
     * Sets the sort field.
     *
     * @param sort Sort field
     */
    public void setSort(String sort) {
        this.sort = sort;
    }

    /**
     * Gets the sort direction.
     *
     * @return Sort direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sets the sort direction.
     *
     * @param direction Sort direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * Gets the current status filter.
     *
     * @return Current status filter
     */
    public Application.ApplicationStatus getCurrentStatus() {
        return currentStatus;
    }

    /**
     * Sets the current status filter.
     *
     * @param currentStatus Current status filter
     */
    public void setCurrentStatus(Application.ApplicationStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    /**
     * Gets the application statistics.
     *
     * @return Application statistics
     */
    public ApplicationStatsDTO getStats() {
        return stats;
    }

    /**
     * Sets the application statistics.
     *
     * @param stats Application statistics
     */
    public void setStats(ApplicationStatsDTO stats) {
        this.stats = stats;
    }
}