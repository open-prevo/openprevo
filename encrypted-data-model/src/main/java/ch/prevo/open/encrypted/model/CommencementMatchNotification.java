package ch.prevo.open.encrypted.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;

/**
 * Matching notification sent to an OpenPrevo Node.
 */
public class CommencementMatchNotification {

    private String encryptedOasiNumber;
    private String retirementFundUid;
    private String referenceId;
    private String previousRetirementFundUid;
    private LocalDate entryDate;

    public String getEncryptedOasiNumber() {
        return encryptedOasiNumber;
    }

    public void setEncryptedOasiNumber(String encryptedOasiNumber) {
        this.encryptedOasiNumber = encryptedOasiNumber;
    }

    public String getRetirementFundUid() {
        return retirementFundUid;
    }

    public void setRetirementFundUid(String retirementFundUid) {
        this.retirementFundUid = retirementFundUid;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getPreviousRetirementFundUid() {
        return previousRetirementFundUid;
    }

    public void setPreviousRetirementFundUid(String previousRetirementFundUid) {
        this.previousRetirementFundUid = previousRetirementFundUid;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("encryptedOasiNumber", encryptedOasiNumber)
                .append("retirementFundUid", retirementFundUid)
                .append("referenceId", referenceId)
                .append("previousRetirementFundUid", previousRetirementFundUid)
                .append("entryDate", entryDate)
                .toString();
    }
}
