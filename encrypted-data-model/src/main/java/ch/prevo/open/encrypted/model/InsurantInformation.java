package ch.prevo.open.encrypted.model;

import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDate;

/**
 * Encrypted information to exchange with OpenPrevo HUB.
 */
public class InsurantInformation implements Comparable<InsurantInformation> {

    private String encryptedOasiNumber;
    private String retirementFundUid;
    private LocalDate date;

    public InsurantInformation() {
    }

    public InsurantInformation(String encryptedOasiNumber, String retirementFundUid) {
        this.encryptedOasiNumber = encryptedOasiNumber;
        this.retirementFundUid = retirementFundUid;
    }

    public InsurantInformation(String encryptedOasiNumber, String retirementFundUid, LocalDate date) {
        this.encryptedOasiNumber = encryptedOasiNumber;
        this.retirementFundUid = retirementFundUid;
        this.date = date;
    }

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

    public LocalDate getDate() {
        return date;
    }

    @Override
    public int compareTo(InsurantInformation o) {
        int oasiComparisonResult = ObjectUtils.compare(encryptedOasiNumber, o.encryptedOasiNumber);
        if (oasiComparisonResult != 0) {
            return oasiComparisonResult;
        }
        // TODO: date
        return ObjectUtils.compare(retirementFundUid, o.retirementFundUid);
    }
}
