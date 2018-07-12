package ch.prevo.open.encrypted.model;

import java.time.LocalDate;

import org.apache.commons.lang3.ObjectUtils;

/**
 * Encrypted information to exchange with OpenPrevo Hub.
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InsurantInformation that = (InsurantInformation) o;

        if (encryptedOasiNumber != null ? !encryptedOasiNumber.equals(that.encryptedOasiNumber) : that.encryptedOasiNumber != null) {
            return false;
        }
        if (retirementFundUid != null ? !retirementFundUid.equals(that.retirementFundUid) : that.retirementFundUid != null) {
            return false;
        }
        return date != null ? date.equals(that.date) : that.date == null;
    }

    @Override
    public int hashCode() {
        int result = encryptedOasiNumber != null ? encryptedOasiNumber.hashCode() : 0;
        result = 31 * result + (retirementFundUid != null ? retirementFundUid.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(InsurantInformation o) {
        int oasiComparisonResult = ObjectUtils.compare(encryptedOasiNumber, o.encryptedOasiNumber);
        if (oasiComparisonResult != 0) {
            return oasiComparisonResult;
        }
        int uidComparisonResult = ObjectUtils.compare(retirementFundUid, o.retirementFundUid);
        if (uidComparisonResult != 0) {
            return uidComparisonResult;
        }
        return ObjectUtils.compare(date, o.date);
    }
}
