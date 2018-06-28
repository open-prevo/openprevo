package ch.prevo.open.encrypted.model;

/**
 * Matching notification sent to an OpenPrevo Node.
 */
public class MatchNotification {

    private String encryptedOasiNumber;
    private String newRetirementFundUid;

    public MatchNotification() {}

    public MatchNotification(String encryptedOasiNumber, String newRetirementFundUid) {
        this.encryptedOasiNumber = encryptedOasiNumber;
        this.newRetirementFundUid = newRetirementFundUid;
    }

    public String getEncryptedOasiNumber() {
        return encryptedOasiNumber;
    }

    public void setEncryptedOasiNumber(String encryptedOasiNumber) {
        this.encryptedOasiNumber = encryptedOasiNumber;
    }

    public String getNewRetirementFundUid() {
        return newRetirementFundUid;
    }

    public void setNewRetirementFundUid(String newRetirementFundUid) {
        this.newRetirementFundUid = newRetirementFundUid;
    }
}
