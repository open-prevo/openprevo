package ch.prevo.open.hub.repository;

import ch.prevo.open.encrypted.model.EncryptedData;
import ch.prevo.open.encrypted.model.MatchForCommencement;
import ch.prevo.open.encrypted.model.MatchForTermination;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan
public class NotificationDAOTest {

    private final String OASI = "756.1335.5778.23";
    private final String previousRetirementFundUID = "CHE-109.740.084";
    private final String newRetirementFundUID = "CHE-109.537.488";
    private final LocalDate commencementDate = LocalDate.of(2018, 7, 1);
    private final LocalDate terminationDate = LocalDate.of(2018, 6, 30);

    @Inject
    private NotificationDAO notificationDAO;

    @Test
    public void isMatchForTerminationAlreadyNotified() {

        // given
        MatchForTermination commencementMatch = getMatchForTermination();
        MatchForTermination duplicateCommencementMatch = getMatchForTermination();

        // when
        boolean initiallyNotMatched = notificationDAO
                .isMatchForTerminationAlreadyNotified(commencementMatch);

        notificationDAO.saveMatchForTermination(commencementMatch);

        boolean commencementMatchAlreadyNotified = notificationDAO
                .isMatchForTerminationAlreadyNotified(commencementMatch);

        boolean duplicateIsAlreadyNotified = notificationDAO
                .isMatchForTerminationAlreadyNotified(duplicateCommencementMatch);

        // then
        assertThat(initiallyNotMatched).isFalse();
        assertThat(commencementMatchAlreadyNotified).isTrue();
        assertThat(duplicateIsAlreadyNotified).isTrue();

    }

    private MatchForTermination getMatchForTermination() {
        EncryptedData transferInformation = new EncryptedData("Key_" + Math.random(), "Data_" + Math.random(), "IV...");
        return new MatchForTermination(OASI, previousRetirementFundUID, newRetirementFundUID,
                commencementDate, terminationDate,
                transferInformation);
    }

    @Test
    public void isMatchForCommencementAlreadyNotified() {

        // given
        MatchForCommencement terminationMatch = getMatchForCommencement();
        MatchForCommencement duplicateTerminationMatch = getMatchForCommencement();

        // when
        boolean initiallyNotMatched = notificationDAO
                .isMatchForCommencementAlreadyNotified(terminationMatch);

        notificationDAO.saveMatchForCommencement(terminationMatch);

        boolean commencementMatchAlreadyNotified = notificationDAO
                .isMatchForCommencementAlreadyNotified(terminationMatch);

        boolean duplicateIsAlreadyNotified = notificationDAO
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