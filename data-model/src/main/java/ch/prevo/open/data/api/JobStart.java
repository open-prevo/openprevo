package ch.prevo.open.data.api;

public class JobStart extends AbstractJobEvent {
	private CapitalTransferInformation capitalTransferInfo;

	public CapitalTransferInformation getCapitalTransferInfo() {
		return capitalTransferInfo;
	}

	public void setCapitalTransferInfo(CapitalTransferInformation capitalTransferInfo) {
		this.capitalTransferInfo = capitalTransferInfo;
	}

    public JobStart() {}

    public JobStart(String techId, JobInfo jobInfo, CapitalTransferInformation capitalTransferInfo) {
        super(techId, jobInfo);
        this.capitalTransferInfo = capitalTransferInfo;
    }
}
