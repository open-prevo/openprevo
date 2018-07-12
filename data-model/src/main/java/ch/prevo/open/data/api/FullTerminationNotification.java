package ch.prevo.open.data.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDate;

public class FullTerminationNotification {

    private String previousRetirementFundUid;
    private LocalDate terminationDate;

    private JobStart jobStart;

    public String getPreviousRetirementFundUid() {
        return previousRetirementFundUid;
    }

    public void setPreviousRetirementFundUid(String previousRetirementFundUid) {
        this.previousRetirementFundUid = previousRetirementFundUid;
    }

    public LocalDate getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(LocalDate terminationDate) {
        this.terminationDate = terminationDate;
    }

    public JobStart getJobStart() {
        return jobStart;
    }

    public void setJobStart(JobStart jobStart) {
        this.jobStart = jobStart;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("previousRetirementFundUid", previousRetirementFundUid)
                .append("terminationDate", terminationDate)
                .append("jobStart", jobStart)
                .toString();
    }
}
