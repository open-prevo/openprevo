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
package ch.prevo.open.hub.repository;

import ch.prevo.open.encrypted.model.MatchNotification;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity(name = "NOTIFICATION")
public class NotificationDTO {

    @Id
    @GeneratedValue
    @Column(name="ID")
    private long id;

    @Column(name="ENCRYPTED_OASI_NUMBER")
    private String encryptedOasiNumber;

    @Column(name="PREVIOUS_RETIREMENT_FUND_UID")
    private String previousRetirementFundUid;

    @Column(name="NEW_RETIREMENT_FUND_UID")
    private String newRetirementFundUid;

    @Column(name="COMMENCEMENT_DATE")
    private LocalDate commencementDate;

    @Column(name="TERMINATION_DATE")
    private LocalDate terminationDate;

    @Column(name="MATCH_FOR_COMMENCEMENT_NOTIFIED")
    private boolean matchForCommencementNotified;

    @Column(name="MATCH_FOR_TERMINATION_NOTIFIED")
    private boolean matchForTerminationNotified;

    public NotificationDTO() {}

    public NotificationDTO(MatchNotification notification) {
        this.encryptedOasiNumber = notification.getEncryptedOasiNumber();
        this.previousRetirementFundUid = notification.getPreviousRetirementFundUid();
        this.newRetirementFundUid = notification.getNewRetirementFundUid();
        this.commencementDate = notification.getCommencementDate();
        this.terminationDate = notification.getTerminationDate();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEncryptedOasiNumber() {
        return encryptedOasiNumber;
    }

    public void setEncryptedOasiNumber(String encryptedOasiNumber) {
        this.encryptedOasiNumber = encryptedOasiNumber;
    }

    public String getPreviousRetirementFundUid() {
        return previousRetirementFundUid;
    }

    public void setPreviousRetirementFundUid(String previousRetirementFundUid) {
        this.previousRetirementFundUid = previousRetirementFundUid;
    }

    public String getNewRetirementFundUid() {
        return newRetirementFundUid;
    }

    public void setNewRetirementFundUid(String newRetirementFundUid) {
        this.newRetirementFundUid = newRetirementFundUid;
    }

    public LocalDate getCommencementDate() {
        return commencementDate;
    }

    public void setCommencementDate(LocalDate commencementDate) {
        this.commencementDate = commencementDate;
    }

    public LocalDate getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(LocalDate terminationDate) {
        this.terminationDate = terminationDate;
    }

    public boolean isMatchForCommencementNotified() {
        return matchForCommencementNotified;
    }

    public void setMatchForCommencementNotified(boolean commencementNotified) {
        this.matchForCommencementNotified = commencementNotified;
    }

    public boolean isMatchForTerminationNotified() {
        return matchForTerminationNotified;
    }

    public void setMatchForTerminationNotified(boolean terminationNotified) {
        this.matchForTerminationNotified = terminationNotified;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("encryptedOasiNumber", encryptedOasiNumber)
                .append("previousRetirementFundUid", previousRetirementFundUid)
                .append("newRetirementFundUid", newRetirementFundUid)
                .append("commencementDate", commencementDate)
                .append("terminationDate", terminationDate)
                .append("matchForCommencementNotified", matchForCommencementNotified)
                .append("matchForTerminationNotified", matchForTerminationNotified)
                .toString();
    }
}
