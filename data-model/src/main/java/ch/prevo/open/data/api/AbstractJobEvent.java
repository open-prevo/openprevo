package ch.prevo.open.data.api;

public abstract class AbstractJobEvent {
	
	private String techId;
	
	public AbstractJobEvent(String techId, JobInfo jobInfo) {
		super();
		this.techId = techId;
		this.jobInfo = jobInfo;
	}

	public String getTechId() {
		return techId;
	}

	private JobInfo jobInfo;

	public JobInfo getJobInfo() {
		return jobInfo;
	}
}
