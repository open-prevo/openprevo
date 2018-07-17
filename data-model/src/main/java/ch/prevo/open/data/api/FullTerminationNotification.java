package ch.prevo.open.data.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDate;

public class FullTerminationNotification {

    private String previousRetirementFundUid;
    private LocalDate terminationDate;

    private EmploymentCommencement employmentCommencement;

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

    public EmploymentCommencement getEmploymentCommencement() {
        return employmentCommencement;
    }

    public void setEmploymentCommencement(EmploymentCommencement employmentCommencement) {
        this.employmentCommencement = employmentCommencement;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("previousRetirementFundUid", previousRetirementFundUid)
                .append("terminationDate", terminationDate)
                .append("employmentCommencement", employmentCommencement)
                .toString();
    }
}
