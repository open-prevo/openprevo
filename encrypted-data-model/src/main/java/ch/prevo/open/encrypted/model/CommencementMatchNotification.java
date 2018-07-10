package ch.prevo.open.encrypted.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;

/**
 * Matching notification sent to an OpenPrevo Node.
 */
public class CommencementMatchNotification {

    private String encryptedOasiNumber;
    private String previousRetirementFundUid;
    private String newRetirementFundUid;
    private LocalDate commencementDate;
    private LocalDate terminationDate;
    private CapitalTransferInformation transferInformation;


    public CommencementMatchNotification() {
    }

    public CommencementMatchNotification(String encryptedOasiNumber, String previousRetirementFundUid, String newRetirementFundUid, LocalDate commencementDate, LocalDate terminationDate, CapitalTransferInformation transferInformation) {
        this.encryptedOasiNumber = encryptedOasiNumber;
        this.previousRetirementFundUid = previousRetirementFundUid;
        this.newRetirementFundUid = newRetirementFundUid;
        this.commencementDate = commencementDate;
        this.terminationDate = terminationDate;
        this.transferInformation = transferInformation;
    }

    public String getEncryptedOasiNumber() {
        return encryptedOasiNumber;
    }

    public void setEncryptedOasiNumber(String encryptedOasiNumber) {
        this.encryptedOasiNumber = encryptedOasiNumber;
    }

    public String getPreviousRetirementFundUid() {
        return previousRetirementFundUid;
    }

    public void setPreviousRetirementFundUid(String previousRetirementFundUid) {
        this.previousRetirementFundUid = previousRetirementFundUid;
    }

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

    public LocalDate getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(LocalDate terminationDate) {
        this.terminationDate = terminationDate;
    }

    public CapitalTransferInformation getTransferInformation() {
        return transferInformation;
    }

    public void setTransferInformation(CapitalTransferInformation transferInformation) {
        this.transferInformation = transferInformation;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("encryptedOasiNumber", encryptedOasiNumber)
                .append("previousRetirementFundUid", previousRetirementFundUid)
                .append("newRetirementFundUid", newRetirementFundUid)
                .append("commencementDate", commencementDate)
                .append("terminationDate", terminationDate)
                .append("transferInformation", transferInformation)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommencementMatchNotification that = (CommencementMatchNotification) o;

        if (encryptedOasiNumber != null ? !encryptedOasiNumber.equals(that.encryptedOasiNumber) : that.encryptedOasiNumber != null)
            return false;
        if (previousRetirementFundUid != null ? !previousRetirementFundUid.equals(that.previousRetirementFundUid) : that.previousRetirementFundUid != null)
            return false;
        if (newRetirementFundUid != null ? !newRetirementFundUid.equals(that.newRetirementFundUid) : that.newRetirementFundUid != null)
            return false;
        if (commencementDate != null ? !commencementDate.equals(that.commencementDate) : that.commencementDate != null) return false;
        if (terminationDate != null ? !terminationDate.equals(that.terminationDate) : that.terminationDate != null) return false;
        return transferInformation != null ? transferInformation.equals(that.transferInformation) : that.transferInformation == null;
    }

    @Override
    public int hashCode() {
        int result = encryptedOasiNumber != null ? encryptedOasiNumber.hashCode() : 0;
        result = 31 * result + (previousRetirementFundUid != null ? previousRetirementFundUid.hashCode() : 0);
        result = 31 * result + (newRetirementFundUid != null ? newRetirementFundUid.hashCode() : 0);
        result = 31 * result + (commencementDate != null ? commencementDate.hashCode() : 0);
        result = 31 * result + (terminationDate != null ? terminationDate.hashCode() : 0);
        result = 31 * result + (transferInformation != null ? transferInformation.hashCode() : 0);
        return result;
    }

}
