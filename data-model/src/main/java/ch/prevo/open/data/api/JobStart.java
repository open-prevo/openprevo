package ch.prevo.open.data.api;

public class JobStart extends AbstractJobEvent {

	public JobStart(String techId, JobInfo jobInfo, CapitalTransferInformation capitalTransferInfo) {
		super(techId, jobInfo);
		this.capitalTransferInfo = capitalTransferInfo;
	}

	private CapitalTransferInformation capitalTransferInfo;

	public CapitalTransferInformation getCapitalTransferInfo() {
		return capitalTransferInfo;
	}
}
