package ch.prevo.open.hub.match;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDate;

public class Match {

    private final String encryptedOasiNumber;
    private final String previousRetirementFundUid;
    private final String newRetirementFundUid;
    private final LocalDate entryDate;
    private final LocalDate exitDate;

    public Match(String encryptedOasiNumber, String previousRetirementFundUid, String newRetirementFundUid, LocalDate entryDate, LocalDate exitDate) {
        this.encryptedOasiNumber = encryptedOasiNumber;
        this.previousRetirementFundUid = previousRetirementFundUid;
        this.newRetirementFundUid = newRetirementFundUid;
        this.entryDate = entryDate;
        this.exitDate = exitDate;
    }

    public String getEncryptedOasiNumber() {
        return encryptedOasiNumber;
    }

    public String getPreviousRetirementFundUid() {
        return previousRetirementFundUid;
    }

    public String getNewRetirementFundUid() {
        return newRetirementFundUid;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public LocalDate getExitDate() {
        return exitDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Match match = (Match) o;

        return new EqualsBuilder()
                .append(encryptedOasiNumber, match.encryptedOasiNumber)
                .append(previousRetirementFundUid, match.previousRetirementFundUid)
                .append(newRetirementFundUid, match.newRetirementFundUid)
                .append(entryDate, match.entryDate)
                .append(exitDate, match.exitDate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(encryptedOasiNumber)
                .append(previousRetirementFundUid)
                .append(newRetirementFundUid)
                .append(entryDate)
                .append(exitDate)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("encryptedOasiNumber", encryptedOasiNumber)
                .append("previousRetirementFundUid", previousRetirementFundUid)
                .append("newRetirementFundUid", newRetirementFundUid)
                .append("entryDate", entryDate)
                .append("exitDate", exitDate)
                .toString();
    }
}
