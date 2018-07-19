package ch.prevo.open.hub.repository;

import ch.prevo.open.encrypted.model.MatchNotification;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class NotificationDTO {

    @Id
    @GeneratedValue
    private long id;

    private String encryptedOasiNumber;
    private String previousRetirementFundUid;
    private String newRetirementFundUid;
    private LocalDate commencementDate;
    private LocalDate terminationDate;

    private boolean matchForCommencementNotified;
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
