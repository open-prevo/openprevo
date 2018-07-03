package ch.prevo.open.hub.nodes;

import java.util.List;

public class NodeConfiguration {

    public static final String JOB_START_ENDPOINT = "/job-start";
    public static final String JOB_END_ENDPOINT = "/job-end";
    public static final String MATCH_NOTIFICATION_ENDPOINT = "/match-notification";

    private List<String> retirementFundUids;
    private String jobExitsUrl;
    private String jobEntriesUrl;
    private String matchNotifyUrl;

    public NodeConfiguration(String baseUrl, List<String> retirementFundUids) {
        this.jobEntriesUrl = baseUrl + JOB_START_ENDPOINT;
        this.jobExitsUrl = baseUrl + JOB_END_ENDPOINT;
        this.matchNotifyUrl = baseUrl + MATCH_NOTIFICATION_ENDPOINT;
        this.retirementFundUids = retirementFundUids;
    }

    NodeConfiguration(String jobExitsUrl, String jobEntriesUrl, String matchNotifyUrl, List<String> retirementFundUids) {
        this.jobExitsUrl = jobExitsUrl;
        this.jobEntriesUrl = jobEntriesUrl;
        this.retirementFundUids = retirementFundUids;
        this.matchNotifyUrl = matchNotifyUrl;
    }

    String getJobExitsUrl() {
        return jobExitsUrl;
    }

    String getJobEntriesUrl() {
        return jobEntriesUrl;
    }

    List<String> getRetirementFundUids() {
        return retirementFundUids;
    }

    String getMatchNotifyUrl() {
        return matchNotifyUrl;
    }

    public boolean containsRetirementFundUid(String retirementFundUid) {
        for (String fundUid : retirementFundUids) {
            if (fundUid.equals(retirementFundUid)) {
                return true;
            }
        }
        return false;
    }
}
