package ch.prevo.open.hub.nodes;


import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.encrypted.model.CommencementMatchNotification;
import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.encrypted.model.TerminationMatchNotification;
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

    @Inject
    public NodeCaller(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
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

    CapitalTransferInformation postCommencementNotification(String commencementMatchNotifyUrl, TerminationMatchNotification matchNotification) {
        try {
            return restTemplate.postForObject(commencementMatchNotifyUrl, matchNotification, CapitalTransferInformation.class);
        } catch (Exception e) {
            // TODO persist information that match needs to be notified later
            LOGGER.error("Could not send notification for match {} to URL {}", matchNotification, commencementMatchNotifyUrl, e);
            return null;
        }
    }

    void postTerminationNotification(String terminationMatchNotifyUrl, CommencementMatchNotification matchNotification) {
        try {
            restTemplate.postForEntity(terminationMatchNotifyUrl, matchNotification, Void.class);
        } catch (Exception e) {
            // TODO persist information that match needs to be notified later
            LOGGER.error("Could not send notification for match {} to URL {}", matchNotification, terminationMatchNotifyUrl, e);
        }
    }
}
