package ch.prevo.open.encrypted.model;

import java.time.LocalDate;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Matching notification sent to an OpenPrevo Node.
 */
public class MatchForCommencement {

    private String encryptedOasiNumber;
    private String retirementFundUid;
    private String previousRetirementFundUid;
    private LocalDate commencementDate;
    private LocalDate terminationDate;

    public MatchForCommencement() {}

    public MatchForCommencement(String encryptedOasiNumber,
                                        String retirementFundUid,
                                        String previousRetirementFundUid,
                                        LocalDate commencementDate,
                                        LocalDate terminationDate) {
        this.encryptedOasiNumber = encryptedOasiNumber;
        this.retirementFundUid = retirementFundUid;
        this.previousRetirementFundUid = previousRetirementFundUid;
        this.commencementDate = commencementDate;
        this.terminationDate = terminationDate;
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

    public String getPreviousRetirementFundUid() {
        return previousRetirementFundUid;
    }

    public void setPreviousRetirementFundUid(String previousRetirementFundUid) {
        this.previousRetirementFundUid = previousRetirementFundUid;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("encryptedOasiNumber", encryptedOasiNumber)
                .append("retirementFundUid", retirementFundUid)
                .append("previousRetirementFundUid", previousRetirementFundUid)
                .append("commencementDate", commencementDate)
                .append("terminationDate", terminationDate)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        MatchForCommencement that = (MatchForCommencement) o;

        if (encryptedOasiNumber != null ?
                !encryptedOasiNumber.equals(that.encryptedOasiNumber) :
                that.encryptedOasiNumber != null)
            return false;
        if (retirementFundUid != null ?
                !retirementFundUid.equals(that.retirementFundUid) :
                that.retirementFundUid != null)
            return false;
        if (previousRetirementFundUid != null ?
                !previousRetirementFundUid.equals(that.previousRetirementFundUid) :
                that.previousRetirementFundUid != null)
            return false;
        if (commencementDate != null ? !commencementDate.equals(that.commencementDate) : that.commencementDate != null)
            return false;
        return terminationDate != null ? terminationDate.equals(that.terminationDate) : that.terminationDate == null;
    }

    @Override
    public int hashCode() {
        int result = encryptedOasiNumber != null ? encryptedOasiNumber.hashCode() : 0;
        result = 31 * result + (retirementFundUid != null ? retirementFundUid.hashCode() : 0);
        result = 31 * result + (previousRetirementFundUid != null ? previousRetirementFundUid.hashCode() : 0);
        result = 31 * result + (commencementDate != null ? commencementDate.hashCode() : 0);
        result = 31 * result + (terminationDate != null ? terminationDate.hashCode() : 0);
        return result;
    }
}
