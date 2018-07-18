package ch.prevo.open.hub.repository;

import ch.prevo.open.encrypted.model.EncryptedData;
import ch.prevo.open.encrypted.model.MatchForCommencement;
import ch.prevo.open.encrypted.model.MatchForTermination;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationRepositoryTest {

    private final String OASI = "756.1335.5778.23";
    private final String previousRetirementFundUID = "CHE-109.740.084";
    private final String newRetirementFundUID = "CHE-109.537.488";
    private final LocalDate commencementDate = LocalDate.of(2018, 7, 1);
    private final LocalDate terminationDate = LocalDate.of(2018, 6, 30);

    private NotificationRepository notificationRepository;

    @Before
    public void setUp() {
        this.notificationRepository = new NotificationRepository();
    }

    @Test
    public void isCommencementMatchAlreadyNotified() {

        // given
        MatchForTermination commencementMatch = getMatchForTermination();
        MatchForTermination duplicateCommencementMatch = getMatchForTermination();

        // when
        notificationRepository = new NotificationRepository();
        boolean initiallyNotMatched = notificationRepository
                .isMatchForTerminationAlreadyNotified(commencementMatch);

        notificationRepository.saveMatchForTermination(commencementMatch);

        boolean commencementMatchAlreadyNotified = notificationRepository
                .isMatchForTerminationAlreadyNotified(commencementMatch);

        boolean duplicateIsAlreadyNotified = notificationRepository
                .isMatchForTerminationAlreadyNotified(duplicateCommencementMatch);

        // then
        assertThat(initiallyNotMatched).isFalse();
        assertThat(commencementMatchAlreadyNotified).isTrue();
        assertThat(duplicateIsAlreadyNotified).isTrue();

    }

    private MatchForTermination getMatchForTermination() {
        EncryptedData transferInformation = new EncryptedData();
        transferInformation.setEncryptedSymmetricKeyBase64("Key_" + Math.random());
        transferInformation.setEncryptedDataBase64("Data_" + Math.random());
        return new MatchForTermination(OASI, previousRetirementFundUID, newRetirementFundUID,
                commencementDate, terminationDate,
                transferInformation);
    }

    @Test
    public void isTerminationMatchAlreadyNotified() {

        // given
        MatchForCommencement terminationMatch = getMatchForCommencement();
        MatchForCommencement duplicateTerminationMatch = getMatchForCommencement();

        // when
        notificationRepository = new NotificationRepository();
        boolean initiallyNotMatched = notificationRepository
                .isMatchForCommencementAlreadyNotified(terminationMatch);

        notificationRepository.saveMatchForCommencement(terminationMatch);

        boolean commencementMatchAlreadyNotified = notificationRepository
                .isMatchForCommencementAlreadyNotified(terminationMatch);

        boolean duplicateIsAlreadyNotified = notificationRepository
                .isMatchForCommencementAlreadyNotified(duplicateTerminationMatch);

        // then
        assertThat(initiallyNotMatched).isFalse();
        assertThat(commencementMatchAlreadyNotified).isTrue();
        assertThat(duplicateIsAlreadyNotified).isTrue();
    }

    private MatchForCommencement getMatchForCommencement() {
        return new MatchForCommencement(OASI, newRetirementFundUID, previousRetirementFundUID,
                commencementDate, terminationDate);
    }
}