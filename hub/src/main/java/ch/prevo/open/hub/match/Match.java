package ch.prevo.open.hub.match;

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

}
