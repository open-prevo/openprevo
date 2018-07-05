package ch.prevo.open.hub.nodes;

import java.util.List;
import java.util.Objects;

public class NodeConfiguration {

    public static final String JOB_START_ENDPOINT = "/job-start";
    public static final String JOB_END_ENDPOINT = "/job-end";
    public static final String MATCH_NOTIFICATION_ENDPOINT = "/match-notification";

    private List<String> retirementFundUids;
    private String jobExitsUrl;
    private String jobEntriesUrl;
    private String matchNotifyUrl;

    public NodeConfiguration() {}

    public NodeConfiguration(String baseUrl, List<String> retirementFundUids) {
        this.jobEntriesUrl = baseUrl + JOB_START_ENDPOINT;
        this.jobExitsUrl = baseUrl + JOB_END_ENDPOINT;
        this.matchNotifyUrl = baseUrl + MATCH_NOTIFICATION_ENDPOINT;
        this.retirementFundUids = retirementFundUids;
    }

    String getJobExitsUrl() {
        return jobExitsUrl;
    }

    public void setJobExitsUrl(String jobExitsUrl) {
        this.jobExitsUrl = jobExitsUrl;
    }

    String getJobEntriesUrl() {
        return jobEntriesUrl;
    }

    public void setJobEntriesUrl(String jobEntriesUrl) {
        this.jobEntriesUrl = jobEntriesUrl;
    }

    List<String> getRetirementFundUids() {
        return retirementFundUids;
    }

    public void setRetirementFundUids(List<String> retirementFundUids) {
        this.retirementFundUids = retirementFundUids;
    }

    String getMatchNotifyUrl() {
        return matchNotifyUrl;
    }

    public void setMatchNotifyUrl(String matchNotifyUrl) {
        this.matchNotifyUrl = matchNotifyUrl;
    }

    public boolean containsRetirementFundUid(String retirementFundUid) {
        return retirementFundUids.stream().anyMatch(s -> Objects.equals(s, retirementFundUid));
    }
}
