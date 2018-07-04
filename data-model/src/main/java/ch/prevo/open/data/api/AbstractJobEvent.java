package ch.prevo.open.data.api;

public abstract class AbstractJobEvent {

    private String techId;

    public String getTechId() {
        return techId;
    }

    public void setTechId(String techId) {
        this.techId = techId;
    }

    private JobInfo jobInfo;

    public JobInfo getJobInfo() {
        return jobInfo;
    }

    public void setJobInfo(JobInfo jobInfo) {
        this.jobInfo = jobInfo;
    }

    public AbstractJobEvent() {
    }

    public AbstractJobEvent(String techId, JobInfo jobInfo) {
        this.techId = techId;
        this.jobInfo = jobInfo;
    }
}
