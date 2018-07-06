package ch.prevo.open.encrypted.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;

/**
 * Matching notification sent to an OpenPrevo Node.
 */
public class TerminationMatchNotification {

    private String encryptedOasiNumber;
    private String retirementFundUid;
    private String newRetirementFundUid;
    private LocalDate entryDate;
    private LocalDate exitDate;
    private CapitalTransferInformation transferInformation;

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

    public String getNewRetirementFundUid() {
        return newRetirementFundUid;
    }

    public void setNewRetirementFundUid(String newRetirementFundUid) {
        this.newRetirementFundUid = newRetirementFundUid;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public LocalDate getExitDate() {
        return exitDate;
    }

    public void setExitDate(LocalDate exitDate) {
        this.exitDate = exitDate;
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
                .append("retirementFundUid", retirementFundUid)
                .append("newRetirementFundUid", newRetirementFundUid)
                .append("entryDate", entryDate)
                .append("exitDate", exitDate)
                .append("transferInformation", transferInformation)
                .toString();
    }
}
