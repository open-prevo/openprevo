package ch.prevo.open.hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationDTO, Long> {

    Optional<NotificationDTO> findByEncryptedOasiNumberAndPreviousRetirementFundUidAndNewRetirementFundUidAndCommencementDateAndTerminationDate(
            String encryptedOasiNumber,
            String previousRetirementFundUid,
            String newRetirementFundUid,
            LocalDate commencementDate,
            LocalDate terminationDate
    );


}
