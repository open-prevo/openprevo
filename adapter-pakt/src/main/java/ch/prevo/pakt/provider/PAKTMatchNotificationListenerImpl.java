package ch.prevo.pakt.provider;

import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ch.prevo.open.data.api.FullCommencementNotification;
import ch.prevo.open.data.api.FullTerminationNotification;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import ch.prevo.pakt.PaktEnvironment;
import ch.prevo.pakt.RetirementFund;
import ch.prevo.service.common.extern.generate.ServiceCore;
import ch.prevo.service.common.extern.generate.ServiceMessage;
import ch.prevo.service.common.meldung.extern.generate.FzlVerwendung;
import ch.prevo.service.common.meldung.extern.generate.ListFZLVerwendung;
import ch.prevo.service.common.meldung.extern.generate.ObjectFactory;
import ch.prevo.service.meldung.update.extern.generate.MeldungUpdate_Service;
import ch.prevo.service.meldung.update.extern.generate.PrevoServiceException;

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
			submitFzlVerwendung(notification);
		} catch (PrevoServiceException e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	private void submitFzlVerwendung(FullCommencementNotification notification)
			throws PrevoServiceException {
		MeldungUpdate_Service meldungUpdate;
		ServiceCore core = new ServiceCore();
		core.setCdMandant(this.getCdMandant());
		core.setCdProfil((short)1);
		core.setCdSchicht((short)1);
		core.setCdSprache((short)1);
		core.setIdUser(this.getUserId());

		try {
			meldungUpdate = new MeldungUpdate_Service(new URL(config.getServiceBaseUrl()+"MeldungUpdate?wsdl"), new QName(
					"http://generate.extern.update.meldung.service.prevo.ch",
					"MeldungUpdate"));

			final Holder<ServiceMessage> message = new Holder<ServiceMessage>();
			final Holder<Long> nrMeld = new Holder<>();
			nrMeld.value = 0L;
			final boolean slFZLDatenChecked = true;
			final ListFZLVerwendung listFzlVerwendungen = new ListFZLVerwendung();
			listFzlVerwendungen.getFzlVerwendung().add(buildFzlVerwendung(notification));
			final String idAuftrag = "";
			
			meldungUpdate.getMeldungUpdatePort().submitFZLVerwendung(core, notification.getEmploymentTermination().getEmploymentInfo().getInternalPersonId(), nrMeld, slFZLDatenChecked, listFzlVerwendungen, idAuftrag, message);
			
		} catch (MalformedURLException e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	private FzlVerwendung buildFzlVerwendung(FullCommencementNotification notification) {
		ObjectFactory factory = new ObjectFactory();
		FzlVerwendung fzlVerwendung = factory.createFzlVerwendung();
		
		XMLGregorianCalendar date = null;
		
		try {
			date = DatatypeFactory.newInstance().newXMLGregorianCalendar(
					notification.getCommencementDate().toString());
		} catch (DatatypeConfigurationException e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		
		fzlVerwendung.setIdPartner(RetirementFund.getById(notification.getNewRetirementFundUid()).getIdPartner());
		fzlVerwendung.setCdZweck((short)1);
		fzlVerwendung.setIdGeschaeft("");
		fzlVerwendung.setSlPartnerChecked(true);
		fzlVerwendung.setCdWhg((short)1);
		fzlVerwendung.setCdGrdAusz((short)0);
		fzlVerwendung.setDtUnterVoll(date);
		fzlVerwendung.setTxtUebw(notification.getEmploymentTermination().getEmploymentInfo().getInternalPersonId());
		fzlVerwendung.setCdAntFzl((short)3);
		
		return fzlVerwendung;
	}

	private String getUserId() {
		return config.getIdUser();
	}

	private short getCdMandant() {
		return config.getCdMandant();
	}
}
