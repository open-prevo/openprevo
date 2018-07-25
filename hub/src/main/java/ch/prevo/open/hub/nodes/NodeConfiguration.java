/*============================================================================*
 * Copyright (c) 2018 - Prevo-System AG and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 3
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR GPL-3.0 WITH Classpath-exception-2.0
 * 
 * Contributors:
 *     Prevo-System AG - initial API and implementation
 *===========================================================================*/
package ch.prevo.open.hub.nodes;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class NodeConfiguration {

    private static final String EMPLOYMENT_COMMENCEMENTS_ENDPOINT = "/commencement-of-employment";
    private static final String EMPLOYMENT_TERMINATION_ENDPOINT = "/termination-of-employment";
    private static final String COMMENCEMENT_MATCH_NOTIFICATION_ENDPOINT = "/commencement-match-notification";
    private static final String TERMINATION_MATCH_NOTIFICATION_ENDPOINT = "/termination-match-notification";

    private List<String> retirementFundUids;
    private String employmentTerminationsUrl;
    private String employmentCommencementsUrl;
    private String commencementMatchNotifyUrl;
    private String terminationMatchNotifyUrl;

    public NodeConfiguration() {}

    public NodeConfiguration(String baseUrl, String... retirementFundUids) {
        this.employmentCommencementsUrl = baseUrl + EMPLOYMENT_COMMENCEMENTS_ENDPOINT;
        this.employmentTerminationsUrl = baseUrl + EMPLOYMENT_TERMINATION_ENDPOINT;
        this.commencementMatchNotifyUrl = baseUrl + COMMENCEMENT_MATCH_NOTIFICATION_ENDPOINT;
        this.terminationMatchNotifyUrl = baseUrl + TERMINATION_MATCH_NOTIFICATION_ENDPOINT;
        this.retirementFundUids = Arrays.asList(retirementFundUids);
    }

    String getEmploymentTerminationsUrl() {
        return employmentTerminationsUrl;
    }

    public void setEmploymentTerminationsUrl(String employmentTerminationsUrl) {
        this.employmentTerminationsUrl = employmentTerminationsUrl;
    }

    String getEmploymentCommencementsUrl() {
        return employmentCommencementsUrl;
    }

    public void setEmploymentCommencementsUrl(String employmentCommencementsUrl) {
        this.employmentCommencementsUrl = employmentCommencementsUrl;
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
                .append("employmentTerminationsUrl", employmentTerminationsUrl)
                .append("employmentCommencementsUrl", employmentCommencementsUrl)
                .append("commencementMatchNotifyUrl", commencementMatchNotifyUrl)
                .append("terminationMatchNotifyUrl", terminationMatchNotifyUrl)
                .toString();
    }
}
