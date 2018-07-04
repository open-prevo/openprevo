package ch.prevo.open.hub.nodes;

import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings("unused")
class NodeConfiguration {

    private String[] retirementFundUids;
    private String jobExitsUrl;
    private String jobEntriesUrl;
    private String matchNotifyUrl;

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

    String[] getRetirementFundUids() {
        return retirementFundUids;
    }

    public void setRetirementFundUids(String[] retirementFundUids) {
        this.retirementFundUids = retirementFundUids;
    }

    String getMatchNotifyUrl() {
        return matchNotifyUrl;
    }

    public void setMatchNotifyUrl(String matchNotifyUrl) {
        this.matchNotifyUrl = matchNotifyUrl;
    }

    public boolean containsRetirementFundUid(String retirementFundUid) {
        return Arrays.stream(retirementFundUids).anyMatch(s -> Objects.equals(s, retirementFundUid));
    }
}
