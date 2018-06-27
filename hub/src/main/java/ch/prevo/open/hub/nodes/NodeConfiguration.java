package ch.prevo.open.hub.nodes;

class NodeConfiguration {

    private String uid;
    private String jobExitsUrl;
    private String jobEntriesUrl;
    private String matchNotifyUrl;

    NodeConfiguration(String uid, String jobExitsUrl, String jobEntriesUrl, String matchNotifyUrl) {
        this.jobExitsUrl = jobExitsUrl;
        this.jobEntriesUrl = jobEntriesUrl;
        this.uid = uid;
        this.matchNotifyUrl = matchNotifyUrl;
    }

    String getJobExitsUrl() {
        return jobExitsUrl;
    }

    String getJobEntriesUrl() {
        return jobEntriesUrl;
    }

    String getUid() {
        return uid;
    }

    String getMatchNotifyUrl() {
        return matchNotifyUrl;
    }
}
