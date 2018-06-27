package ch.prevo.open.hub;

public class NodeConfiguration {

    private String uid;
    private String jobExitsUrl;
    private String jobEntriesUrl;
    private String matchNotifyUrl;

    public NodeConfiguration(String uid, String jobExitsUrl, String jobEntriesUrl, String matchNotifyUrl) {
        this.jobExitsUrl = jobExitsUrl;
        this.jobEntriesUrl = jobEntriesUrl;
        this.uid = uid;
        this.matchNotifyUrl = matchNotifyUrl;
    }

    public String getJobExitsUrl() {
        return jobExitsUrl;
    }

    public String getJobEntriesUrl() {
        return jobEntriesUrl;
    }

    public String getUid() {
        return uid;
    }

    public String getMatchNotifyUrl() {
        return matchNotifyUrl;
    }
}
