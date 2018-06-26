package ch.prevo.open.encrypted.model;

/**
 * Encrypted information to exchange with OpenPrevo HUB.
 */
public class InsurantInformation {

    private String encryptedOasiNumber;
    private String retirementFundUid;

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
}
