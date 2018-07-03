package ch.prevo.open.encrypted.model;

import org.apache.commons.lang3.ObjectUtils;

/**
 * Encrypted information to exchange with OpenPrevo HUB.
 */
public class InsurantInformation implements Comparable<InsurantInformation> {

    private String encryptedOasiNumber;
    private String retirementFundUid;


    public InsurantInformation() {
    }

    public InsurantInformation(String encryptedOasiNumber, String retirementFundUid) {
        this.encryptedOasiNumber = encryptedOasiNumber;
        this.retirementFundUid = retirementFundUid;
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

    @Override
    public int compareTo(InsurantInformation o) {
        int oasiComparisonResult = ObjectUtils.compare(encryptedOasiNumber, o.encryptedOasiNumber);
        if (oasiComparisonResult != 0) {
            return oasiComparisonResult;
        }
        return ObjectUtils.compare(retirementFundUid, o.retirementFundUid);
    }
}
