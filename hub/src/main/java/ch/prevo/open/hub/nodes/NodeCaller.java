package ch.prevo.open.hub.nodes;

import ch.prevo.open.encrypted.model.EncryptedData;
import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.encrypted.model.MatchForCommencement;
import ch.prevo.open.encrypted.model.MatchForTermination;
import ch.prevo.open.hub.repository.NotificationDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

@Service
public class NodeCaller {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeCaller.class);

    private final RestTemplate restTemplate;

    private final NotificationDAO notificationDAO;

    @Inject
    public NodeCaller(RestTemplateBuilder restTemplateBuilder,
                      NotificationDAO notificationDAO) {
        this.restTemplate = restTemplateBuilder.build();
        this.notificationDAO = notificationDAO;
    }

    List<InsurantInformation> getInsurantInformationList(String url) {
        try {
            InsurantInformation[] nodeExits = restTemplate.getForObject(url, InsurantInformation[].class);
            return nodeExits == null ? emptyList() : asList(nodeExits);
        } catch (Exception e) {
            LOGGER.error("Could not fetch data from URL {}", url, e);
            return emptyList();
        }
    }

    EncryptedData postCommencementNotification(String commencementMatchNotifyUrl, MatchForCommencement matchNotification) {
        try {
            if (!notificationDAO.isMatchForCommencementAlreadyNotified(matchNotification)) {
                LOGGER.debug("Send termination match notification for match: {}", matchNotification);
                EncryptedData encryptedCapitalTransferInfo = restTemplate
                        .postForObject(commencementMatchNotifyUrl, matchNotification, EncryptedData.class);

                notificationDAO.saveMatchForCommencement(matchNotification);

                return encryptedCapitalTransferInfo;
            }
        } catch (Exception e) {
            // TODO persist information that match needs to be notified later
            LOGGER.error("Could not send notification for match {} to URL {}", matchNotification,
                    commencementMatchNotifyUrl, e);
        }
        return null;
    }

    void postTerminationNotification(String terminationMatchNotifyUrl, MatchForTermination matchNotification) {
        try {
            if (!notificationDAO.isMatchForTerminationAlreadyNotified(matchNotification)) {
                LOGGER.debug("Send commencement match notification for match: {}", matchNotification);
                restTemplate.postForEntity(terminationMatchNotifyUrl, matchNotification, Void.class);
                notificationDAO.saveMatchForTermination(matchNotification);
            }
        } catch (Exception e) {
            // TODO persist information that match needs to be notified later
            LOGGER.error("Could not send notification for match {} to URL {}", matchNotification,
                    terminationMatchNotifyUrl, e);
        }
    }
}
