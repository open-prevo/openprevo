package ch.prevo.open.hub.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import ch.prevo.open.encrypted.model.Address;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.CommencementMatchNotification;
import ch.prevo.open.encrypted.model.TerminationMatchNotification;

public class NotificationRepositoryTest {

    private final String OASI = "756.1335.5778.23";
    private final String previousRetirementFundUID = "CHE-109.740.084";
    private final String newRetirementFundUID = "CHE-109.537.488";
    private final LocalDate commencementDate = LocalDate.of(2018,7,1);
    private final LocalDate terminationDate = LocalDate.of(2018,6,30);

    private NotificationRepository notificationRepository;

    @Before
    public void setUp() {
        this.notificationRepository = new NotificationRepository();
    }

    @Test
    public void isCommencementMatchAlreadyNotified() {

        // given
        CommencementMatchNotification commencementMatch = getCommencementMatch();
        CommencementMatchNotification duplicateCommencementMatch = getCommencementMatch();

        // when
        notificationRepository = new NotificationRepository();
        boolean initiallyNotMatched = notificationRepository
                .isCommencementMatchAlreadyNotified(commencementMatch);

        notificationRepository.saveCommencementMatchNotification(commencementMatch);

        boolean commencementMatchAlreadyNotified = notificationRepository
                .isCommencementMatchAlreadyNotified(commencementMatch);

        boolean duplicateIsAlreadyNotified = notificationRepository
                .isCommencementMatchAlreadyNotified(duplicateCommencementMatch);

        // then
        assertThat(initiallyNotMatched).isFalse();
        assertThat(commencementMatchAlreadyNotified).isTrue();
        assertThat(duplicateIsAlreadyNotified).isTrue();

    }

    private CommencementMatchNotification getCommencementMatch() {
        Address address = new Address("Baslerstrasse", "4000", "Basel");
        CapitalTransferInformation capitalTransferInformation = new CapitalTransferInformation("BKB", "Basler Kantonalbank",
                address, "CH53 0077 0016 02222 3334 4");

        return new CommencementMatchNotification(OASI, previousRetirementFundUID, newRetirementFundUID,
                commencementDate, terminationDate,
                capitalTransferInformation);
    }

    @Test
    public void isTerminationMatchAlreadyNotified() {

        // given
        TerminationMatchNotification terminationMatch = getTerminationMatch();
        TerminationMatchNotification duplicateTerminationMatch = getTerminationMatch();

        // when
        notificationRepository = new NotificationRepository();
        boolean initiallyNotMatched = notificationRepository
                .isTerminationMatchAlreadyNotified(terminationMatch);

        notificationRepository.saveTerminationMatchNotification(terminationMatch);

        boolean commencementMatchAlreadyNotified = notificationRepository
                .isTerminationMatchAlreadyNotified(terminationMatch);

        boolean duplicateIsAlreadyNotified = notificationRepository
                .isTerminationMatchAlreadyNotified(duplicateTerminationMatch);

        // then
        assertThat(initiallyNotMatched).isFalse();
        assertThat(commencementMatchAlreadyNotified).isTrue();
        assertThat(duplicateIsAlreadyNotified).isTrue();
    }

    private TerminationMatchNotification getTerminationMatch() {
        return new TerminationMatchNotification(OASI, newRetirementFundUID, previousRetirementFundUID,
                commencementDate, terminationDate);
    }
}