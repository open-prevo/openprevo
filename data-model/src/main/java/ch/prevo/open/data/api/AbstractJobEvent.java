package ch.prevo.open.data.api;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("unused")
abstract class AbstractJobEvent {

    private String techId;
    private JobInfo jobInfo;

    AbstractJobEvent() {
    }

    AbstractJobEvent(String techId, JobInfo jobInfo) {
        this.techId = techId;
        this.jobInfo = jobInfo;
    }

    public String getTechId() {
        return techId;
    }

    public void setTechId(String techId) {
        this.techId = techId;
    }

    public JobInfo getJobInfo() {
        return jobInfo;
    }

    public void setJobInfo(JobInfo jobInfo) {
        this.jobInfo = jobInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("techId", techId)
                .append("jobInfo", jobInfo)
                .toString();
    }
}
