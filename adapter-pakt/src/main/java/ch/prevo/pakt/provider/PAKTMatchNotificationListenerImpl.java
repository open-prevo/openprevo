package ch.prevo.pakt.provider;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ch.prevo.open.data.api.FullCommencementNotification;
import ch.prevo.open.data.api.FullTerminationNotification;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import ch.prevo.pakt.config.PaktEnvironment;
import ch.prevo.pakt.config.RetirementFundRegistry;
import ch.prevo.pakt.soap.SoapRequest;
import ch.prevo.pakt.soap.SubmitFZLVerwendungRequestPopulator;

@Service
public class PAKTMatchNotificationListenerImpl implements MatchNotificationListener {

	private static final Logger LOG = LoggerFactory.getLogger(PAKTMatchNotificationListenerImpl.class);

	private final PaktEnvironment config;
    private final RetirementFundRegistry retirementFundRegistry;
	private final SoapRequest soapRequest;
    
    @Inject
	public PAKTMatchNotificationListenerImpl(PaktEnvironment config,
                                             RetirementFundRegistry retirementFundRegistry,
                                             SoapRequest soapRequest) {

	    this.config = config;
	    this.retirementFundRegistry = retirementFundRegistry;
	    this.soapRequest = soapRequest;
    }

	@Override
	public void handleTerminationMatch(FullTerminationNotification notification) {
		LOG.info("Handling FullTerminationNotification notification for OASI [{}]", notification.getEmploymentCommencement().getEmploymentInfo().getOasiNumber());
	}

	@Override
	public void handleCommencementMatch(FullCommencementNotification notification) {
		try {
			LOG.info("Submitting FullCommencementNotification message to PAKT for OASI [{}]", notification.getEmploymentTermination().getEmploymentInfo().getOasiNumber());
			soapRequest.callSubmitFZLVerwendung(config.getServiceBaseUrl(),
					new SubmitFZLVerwendungRequestPopulator(this.getCdMandant(), this.getUserId(),
							notification.getEmploymentTermination().getEmploymentInfo().getInternalPersonId(),
							retirementFundRegistry.getByUid(notification.getNewRetirementFundUid()).getIdPartner(),
							notification.getCommencementDate(),
							notification.getEmploymentTermination().getEmploymentInfo().getInternalPersonId()),
					(doc) -> {
						LOG.info("Message was successfuly submitted to PAKT for OASI [{}] and received number [{}]", notification.getEmploymentTermination().getEmploymentInfo().getOasiNumber(), doc.getElementsByTagName("nrMeld").item(0).getTextContent());
					});
		} catch (Exception e) {
			LOG.error("Error by submitting FullCommencementNotification message to PAKT", e);
			throw new RuntimeException(e);
		}
	}

	private String getUserId() {
		return config.getIdUser();
	}

	private short getCdMandant() {
		return config.getCdMandant();
	}
}
