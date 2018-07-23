package ch.prevo.open.hub.repository;

import ch.prevo.open.encrypted.model.MatchForCommencement;
import ch.prevo.open.encrypted.model.MatchForTermination;
import ch.prevo.open.encrypted.model.MatchNotification;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Optional;

@Service
public class NotificationDAO {

    private final NotificationRepository repository;

    @Inject
    public NotificationDAO(NotificationRepository repository) {
        this.repository = repository;
    }

    public void saveMatchForTermination(MatchForTermination notification) {
        final NotificationDTO dto = findByNotification(notification)
                .orElseGet(() -> new NotificationDTO(notification));

        dto.setMatchForTerminationNotified(true);
        repository.save(dto);
    }

    public void saveMatchForCommencement(MatchForCommencement notification) {
        final NotificationDTO dto = findByNotification(notification)
                .orElseGet(
                        () -> new NotificationDTO(notification)
                );

        dto.setMatchForCommencementNotified(true);
        repository.save(dto);
    }

    public boolean isMatchForTerminationAlreadyNotified(MatchForTermination notification) {
        return findByNotification(notification)
                .map(NotificationDTO::isMatchForTerminationNotified)
                .orElse(false);
    }

    public boolean isMatchForCommencementAlreadyNotified(MatchForCommencement notification) {
        return findByNotification(notification)
                .map(NotificationDTO::isMatchForCommencementNotified)
                .orElse(false);
    }

    private Optional<NotificationDTO> findByNotification(MatchNotification notification) {
        return repository.findByEncryptedOasiNumberAndPreviousRetirementFundUidAndNewRetirementFundUidAndCommencementDateAndTerminationDate(
                notification.getEncryptedOasiNumber(),
                notification.getPreviousRetirementFundUid(),
                notification.getNewRetirementFundUid(),
                notification.getCommencementDate(),
                notification.getTerminationDate()
        );
    }
}
