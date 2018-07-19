package ch.prevo.open.encrypted.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDate;

/**
 * Matching notification sent to an OpenPrevo Node.
 */
public class MatchForTermination extends MatchNotification {

    private CapitalTransferInformation transferInformation;

    public MatchForTermination() {
    }

    public MatchForTermination(String encryptedOasiNumber, String previousRetirementFundUid, String newRetirementFundUid, LocalDate commencementDate, LocalDate terminationDate, CapitalTransferInformation transferInformation) {
        super(encryptedOasiNumber, previousRetirementFundUid, newRetirementFundUid, commencementDate, terminationDate);
        this.transferInformation = transferInformation;
    }

    public CapitalTransferInformation getTransferInformation() {
        return transferInformation;
    }

    public void setTransferInformation(CapitalTransferInformation transferInformation) {
        this.transferInformation = transferInformation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MatchForTermination that = (MatchForTermination) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(transferInformation, that.transferInformation)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(transferInformation)
                .toHashCode();
    }
}
