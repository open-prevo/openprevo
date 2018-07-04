package ch.prevo.open.data.api;

public class JobInfo {

    private String retirementFundUid;

    private String internalReferenz;

    private String oasiNumber;

    private String internalPersonId;

    public JobInfo() {
    }

    public JobInfo(String retirementFundUid, String internalReferenz, String oasiNumber, String internalPersonId) {
        this.retirementFundUid = retirementFundUid;
        this.internalReferenz = internalReferenz;
        this.oasiNumber = oasiNumber;
        this.internalPersonId = internalPersonId;
    }

    public String getRetirementFundUid() {
        return retirementFundUid;
    }

    public void setRetirementFundUid(String retirementFundUid) {
        this.retirementFundUid = retirementFundUid;
    }

    public String getInternalReferenz() {
        return internalReferenz;
    }

    public void setInternalReferenz(String internalReferenz) {
        this.internalReferenz = internalReferenz;
    }

    public String getOasiNumber() {
        return oasiNumber;
    }

    public void setOasiNumber(String oasiNumber) {
        this.oasiNumber = oasiNumber;
    }

    public String getInternalPersonId() {
        return internalPersonId;
    }

    public void setInternalPersonId(String internalPersonId) {
        this.internalPersonId = internalPersonId;
    }

}
