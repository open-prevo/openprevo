package ch.prevo.open.data.api;

public class JobEnd extends AbstractJobEvent {

    public JobEnd() {
    }

    public JobEnd(String techId, JobInfo jobInfo) {
        super(techId, jobInfo);
    }
}
