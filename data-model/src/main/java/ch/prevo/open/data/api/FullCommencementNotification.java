package ch.prevo.open.data.api;

import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDate;

public class FullCommencementNotification {

    private String newRetirementFundUid;
    private LocalDate commencementDate;
    private CapitalTransferInformation transferInformation;

    private EmploymentTermination jobEnd;

    public String getNewRetirementFundUid() {
        return newRetirementFundUid;
    }

    public void setNewRetirementFundUid(String newRetirementFundUid) {
        this.newRetirementFundUid = newRetirementFundUid;
    }

    public LocalDate getCommencementDate() {
        return commencementDate;
    }

    public void setCommencementDate(LocalDate commencementDate) {
        this.commencementDate = commencementDate;
    }

    public CapitalTransferInformation getTransferInformation() {
        return transferInformation;
    }

    public void setTransferInformation(CapitalTransferInformation transferInformation) {
        this.transferInformation = transferInformation;
    }

    public EmploymentTermination getEmploymentTermination() {
        return jobEnd;
    }

    public void setEmploymentTermination(EmploymentTermination jobEnd) {
        this.jobEnd = jobEnd;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("newRetirementFundUid", newRetirementFundUid)
                .append("commencementDate", commencementDate)
                .append("transferInformation", transferInformation)
                .append("jobEnd", jobEnd)
                .toString();
    }
}
