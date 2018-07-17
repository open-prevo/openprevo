package ch.prevo.pakt.provider;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ch.prevo.open.data.api.FullCommencementNotification;
import ch.prevo.open.data.api.FullTerminationNotification;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import ch.prevo.pakt.PaktEnvironment;
import ch.prevo.pakt.RetirementFund;
import ch.prevo.pakt.utils.SoapRequest;
import ch.prevo.pakt.utils.SubmitFZLVerwendungRequestPopulator;

@Service
public class PAKTMatchNotificationListenerImpl implements MatchNotificationListener {

	private static Logger LOG = LoggerFactory.getLogger(PAKTMatchNotificationListenerImpl.class);

	@Inject
	private PaktEnvironment config;

	@Override
	public void handleTerminationMatch(FullTerminationNotification notification) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleCommencementMatch(FullCommencementNotification notification) {
		try {
			SoapRequest.callSubmitFZLVerwendung(config.getServiceBaseUrl(),
					new SubmitFZLVerwendungRequestPopulator(this.getCdMandant(), this.getUserId(),
							notification.getEmploymentTermination().getEmploymentInfo().getInternalPersonId(),
							RetirementFund.getById(notification.getNewRetirementFundUid()).getIdPartner(),
							notification.getCommencementDate(),
							notification.getEmploymentTermination().getEmploymentInfo().getInternalPersonId()),
					(doc) -> {
					});
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
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
