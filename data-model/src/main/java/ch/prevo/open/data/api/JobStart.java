package ch.prevo.open.data.api;

public class JobStart extends AbstractJobEvent {
	public JobStart(String techId, JobInfo jobInfo) {
		super(techId, jobInfo);
	}

	private CapitalTransferInformation capitalTransferInfo;

	public CapitalTransferInformation getCapitalTransferInfo() {
		return capitalTransferInfo;
	}

	public JobStart setCapitalTransferInfo(CapitalTransferInformation capitalTransferInfo) {
		this.capitalTransferInfo = capitalTransferInfo;
		return this;
	}
}
