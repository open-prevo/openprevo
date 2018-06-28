package ch.prevo.open.hub.nodes;

class NodeConfiguration {

    private String[] retirementFundUids;
    private String jobExitsUrl;
    private String jobEntriesUrl;
    private String matchNotifyUrl;

    NodeConfiguration(String jobExitsUrl, String jobEntriesUrl, String matchNotifyUrl, String... retirementFundUids) {
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

    String[] getRetirementFundUids() {
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
