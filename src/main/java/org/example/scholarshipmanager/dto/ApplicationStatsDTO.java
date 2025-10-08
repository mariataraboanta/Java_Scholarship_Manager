package org.example.scholarshipmanager.dto;

/**
 * Data Transfer Object (DTO) used to encapsulate statistics about scholarship applications.
 * It includes counts for different application statuses (pending, approved, rejected)
 * and provides utility methods to calculate percentages for each status.
 */
public class ApplicationStatsDTO {

    private long pendingCount;
    private long approvedCount;
    private long rejectedCount;
    private long totalCount;

    /**
     * Default constructor.
     */
    public ApplicationStatsDTO() {}

    /**
     * Constructs an ApplicationStatsDTO with specific counts.
     *
     * @param pendingCount  the number of pending applications
     * @param approvedCount the number of approved applications
     * @param rejectedCount the number of rejected applications
     * @param totalCount    the total number of applications
     */
    public ApplicationStatsDTO(long pendingCount, long approvedCount, long rejectedCount, long totalCount) {
        this.pendingCount = pendingCount;
        this.approvedCount = approvedCount;
        this.rejectedCount = rejectedCount;
        this.totalCount = totalCount;
    }

    /**
     * @return the number of pending applications
     */
    public long getPendingCount() {
        return pendingCount;
    }

    /**
     * Sets the number of pending applications.
     *
     * @param pendingCount the new count of pending applications
     */
    public void setPendingCount(long pendingCount) {
        this.pendingCount = pendingCount;
    }

    /**
     * @return the number of approved applications
     */
    public long getApprovedCount() {
        return approvedCount;
    }

    /**
     * Sets the number of approved applications.
     *
     * @param approvedCount the new count of approved applications
     */
    public void setApprovedCount(long approvedCount) {
        this.approvedCount = approvedCount;
    }

    /**
     * @return the number of rejected applications
     */
    public long getRejectedCount() {
        return rejectedCount;
    }

    /**
     * Sets the number of rejected applications.
     *
     * @param rejectedCount the new count of rejected applications
     */
    public void setRejectedCount(long rejectedCount) {
        this.rejectedCount = rejectedCount;
    }

    /**
     * @return the total number of applications
     */
    public long getTotalCount() {
        return totalCount;
    }

    /**
     * Sets the total number of applications.
     *
     * @param totalCount the new total count of applications
     */
    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * Calculates the percentage of pending applications.
     *
     * @return the pending percentage, or 0 if totalCount is 0
     */
    public double getPendingPercentage() {
        return totalCount > 0 ? (double) pendingCount / totalCount * 100 : 0;
    }

    /**
     * Calculates the percentage of approved applications.
     *
     * @return the approved percentage, or 0 if totalCount is 0
     */
    public double getApprovedPercentage() {
        return totalCount > 0 ? (double) approvedCount / totalCount * 100 : 0;
    }

    /**
     * Calculates the percentage of rejected applications.
     *
     * @return the rejected percentage, or 0 if totalCount is 0
     */
    public double getRejectedPercentage() {
        return totalCount > 0 ? (double) rejectedCount / totalCount * 100 : 0;
    }
}
