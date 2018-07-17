package ch.prevo.open.hub.nodes;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class NodeConfiguration {

    private static final String JOB_START_ENDPOINT = "/commencement-of-employment";
    private static final String JOB_END_ENDPOINT = "/termination-of-employment";
    private static final String COMMENCEMENT_MATCH_NOTIFICATION_ENDPOINT = "/commencement-match-notification";
    private static final String TERMINATION_MATCH_NOTIFICATION_ENDPOINT = "/termination-match-notification";

    private List<String> retirementFundUids;
    private String employmentExitsUrl;
    private String employmentEntriesUrl;
    private String commencementMatchNotifyUrl;
    private String terminationMatchNotifyUrl;

    public NodeConfiguration() {}

    public NodeConfiguration(String baseUrl, String... retirementFundUids) {
        this.employmentEntriesUrl = baseUrl + JOB_START_ENDPOINT;
        this.employmentExitsUrl = baseUrl + JOB_END_ENDPOINT;
        this.commencementMatchNotifyUrl = baseUrl + COMMENCEMENT_MATCH_NOTIFICATION_ENDPOINT;
        this.terminationMatchNotifyUrl = baseUrl + TERMINATION_MATCH_NOTIFICATION_ENDPOINT;
        this.retirementFundUids = Arrays.asList(retirementFundUids);
    }

    String getEmploymentExitsUrl() {
        return employmentExitsUrl;
    }

    public void setEmploymentExitsUrl(String employmentExitsUrl) {
        this.employmentExitsUrl = employmentExitsUrl;
    }

    String getEmploymentEntriesUrl() {
        return employmentEntriesUrl;
    }

    public void setEmploymentEntriesUrl(String employmentEntriesUrl) {
        this.employmentEntriesUrl = employmentEntriesUrl;
    }

    List<String> getRetirementFundUids() {
        return retirementFundUids;
    }

    public void setRetirementFundUids(List<String> retirementFundUids) {
        this.retirementFundUids = retirementFundUids;
    }

    public String getCommencementMatchNotifyUrl() {
        return commencementMatchNotifyUrl;
    }

    public void setCommencementMatchNotifyUrl(String commencementMatchNotifyUrl) {
        this.commencementMatchNotifyUrl = commencementMatchNotifyUrl;
    }

    public String getTerminationMatchNotifyUrl() {
        return terminationMatchNotifyUrl;
    }

    public void setTerminationMatchNotifyUrl(String terminationMatchNotifyUrl) {
        this.terminationMatchNotifyUrl = terminationMatchNotifyUrl;
    }

    boolean containsRetirementFundUid(String retirementFundUid) {
        return retirementFundUids.stream().anyMatch(s -> Objects.equals(s, retirementFundUid));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("retirementFundUids", retirementFundUids)
                .append("employmentExitsUrl", employmentExitsUrl)
                .append("employmentEntriesUrl", employmentEntriesUrl)
                .append("commencementMatchNotifyUrl", commencementMatchNotifyUrl)
                .append("terminationMatchNotifyUrl", terminationMatchNotifyUrl)
                .toString();
    }
}
