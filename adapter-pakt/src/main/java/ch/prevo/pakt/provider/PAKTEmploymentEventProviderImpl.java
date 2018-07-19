package ch.prevo.pakt.provider;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.data.api.EmploymentInfo;
import ch.prevo.open.data.api.EmploymentTermination;
import ch.prevo.open.encrypted.model.Address;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.node.data.provider.EmploymentCommencementProvider;
import ch.prevo.open.node.data.provider.EmploymentTerminationProvider;
import ch.prevo.pakt.config.PaktEnvironment;
import ch.prevo.pakt.config.RetirementFundRegistry;
import ch.prevo.pakt.entities.TozsPtverm;
import ch.prevo.pakt.repository.PartnerVermittlungRepository;
import ch.prevo.pakt.zd.utils.CdMeld;

@Service
public class PAKTEmploymentEventProviderImpl implements EmploymentTerminationProvider, EmploymentCommencementProvider {

    private static Logger LOG = LoggerFactory.getLogger(PAKTEmploymentEventProviderImpl.class);
	
    @Inject
    private PartnerVermittlungRepository repository;

    @Inject
    private PaktEnvironment config;
    
    @Inject
    private RetirementFundRegistry retirementFundRegistry;
	
	@Override
	public List<EmploymentTermination> getEmploymentTerminations() {
		return repository.findByIdCdmandantAndCdmeld(getCdMandant(), CdMeld.DADURCHF.getCode()).stream()
				.map(ptVerm -> buildEmploymentTermination(ptVerm)).collect(Collectors.toList());
	}

    private EmploymentTermination buildEmploymentTermination(TozsPtverm ptVerm) {
        return new EmploymentTermination(Integer.toString(ptVerm.getId().getId()), buildEmploymentInfo(ptVerm));
    }

    private EmploymentInfo buildEmploymentInfo(TozsPtverm ptVerm) {
    	EmploymentInfo employmentInfo = new EmploymentInfo();
    	employmentInfo.setOasiNumber(ptVerm.getAhv());
    	employmentInfo.setRetirementFundUid(getRetirementFundUid(ptVerm));
    	employmentInfo.setInternalPersonId(ptVerm.getIdgeschaeftpol());
    	employmentInfo.setInternalReferenz(ptVerm.getNameve());
    	employmentInfo.setDate(ptVerm.getDtgueltab());
        return employmentInfo;

    }

    private String getRetirementFundUid(TozsPtverm ptVerm) {
        return retirementFundRegistry.getByCdStf(ptVerm.getCdstf()).getUid();
    }

	@Override
	public List<EmploymentCommencement> getEmploymentCommencements() {
		return repository.findByIdCdmandantAndCdmeld(getCdMandant(), CdMeld.NEUEINTRERF.getCode()).stream()
				.map(ptVerm -> buildEmploymentCommencement(ptVerm)).collect(Collectors.toList());
	}

    private EmploymentCommencement buildEmploymentCommencement(TozsPtverm ptVerm) {
        return new EmploymentCommencement(Integer.toString(ptVerm.getId().getId()), buildEmploymentInfo(ptVerm),
                buildCapitalTransferInformation(ptVerm));
    }

    private CapitalTransferInformation buildCapitalTransferInformation(TozsPtverm ptVerm) {
        CapitalTransferInformation capitalTransferInfo = new CapitalTransferInformation(ptVerm.getNameve(),
                ptVerm.getTxtiban());
        capitalTransferInfo.setReferenceId(ptVerm.getIdgeschaeftpol());
        capitalTransferInfo.setAddress(buildAddress(ptVerm));
        capitalTransferInfo.setAdditionalName(ptVerm.getNamezusatz());
        return capitalTransferInfo;
    }

    private Address buildAddress(TozsPtverm ptVerm) {
        Address address = new Address();
        address.setCity(ptVerm.getTxtort());
        address.setPostalCode(ptVerm.getTxtpostleitzahl());
        address.setStreet(ptVerm.getNamestrasse());
        return address;
    }
    
	private Short getCdMandant() {
		return config.getCdMandant();
	}
}
