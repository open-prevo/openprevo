package ch.prevo.open.hub.match;

import java.util.Objects;

public class Match {

    private final String encryptedOasiNumber;
    private final String previousRetirementFundUid;
    private final String newRetirementFundUid;

    public Match(String encryptedOasiNumber, String previousRetirementFundUid, String newRetirementFundUid) {
        this.encryptedOasiNumber = encryptedOasiNumber;
        this.previousRetirementFundUid = previousRetirementFundUid;
        this.newRetirementFundUid = newRetirementFundUid;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Match match = (Match) o;
        return Objects.equals(encryptedOasiNumber, match.encryptedOasiNumber) &&
                Objects.equals(previousRetirementFundUid, match.previousRetirementFundUid) &&
                Objects.equals(newRetirementFundUid, match.newRetirementFundUid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(encryptedOasiNumber, previousRetirementFundUid, newRetirementFundUid);
    }

    @Override
    public String toString() {
        return "Match{" +
                "encryptedOasiNumber='" + encryptedOasiNumber + '\'' +
                ", previousRetirementFundUid='" + previousRetirementFundUid + '\'' +
                ", newRetirementFundUid='" + newRetirementFundUid + '\'' +
                '}';
    }
}
