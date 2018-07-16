package ch.prevo.pakt.provider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ch.prevo.open.data.api.JobEnd;
import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.data.api.JobStart;
import ch.prevo.open.encrypted.model.Address;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.node.data.provider.JobEndProvider;
import ch.prevo.open.node.data.provider.JobStartProvider;
import ch.prevo.pakt.PaktAdapterConfig;
import ch.prevo.pakt.RetirementFund;
import ch.prevo.pakt.entities.TozsPtverm;
import ch.prevo.pakt.repository.PartnerVermittlungRepository;
import ch.prevo.pakt.zd.utils.CdMeld;

@Service
public class PAKTJobEventProviderImpl implements JobEndProvider, JobStartProvider {

    private static Logger LOG = LoggerFactory.getLogger(PAKTJobEventProviderImpl.class);
	
    private final PartnerVermittlungRepository repository;

    private final PaktAdapterConfig config;
    
	@Inject
    public PAKTJobEventProviderImpl(PaktAdapterConfig config, PartnerVermittlungRepository partnerVermittlungRepository) {
        this.repository = partnerVermittlungRepository;
        this.config = config;
    }

    @Override
    public List<JobEnd> getJobEnds() {
        final List<JobEnd> jobEnds = new ArrayList<>();

		repository.findByIdCdmandantAndCdmeld(getCdMandant(), CdMeld.DADURCHF.getCode()).forEach(ptVerm -> {
			jobEnds.add(buildJobEnd(ptVerm));
		});
        return jobEnds;
    }

    private JobEnd buildJobEnd(TozsPtverm ptVerm) {
        return new JobEnd(Integer.toString(ptVerm.getId().getId()), buildJobInfo(ptVerm));
    }

    private JobInfo buildJobInfo(TozsPtverm ptVerm) {
        JobInfo jobInfo = new JobInfo();
        jobInfo.setOasiNumber(ptVerm.getAhv());
        jobInfo.setRetirementFundUid(getRetirementFundId(ptVerm));
        jobInfo.setInternalPersonId(ptVerm.getIdgeschaeftpol());
        jobInfo.setInternalReferenz(ptVerm.getNameve());
        jobInfo.setDate(ptVerm.getDtgueltab());
        return jobInfo;

    }

    private String getRetirementFundId(TozsPtverm ptVerm) {
        return RetirementFund.getByCdStf(ptVerm.getCdstf()).getId();
    }

    @Override
    public List<JobStart> getJobStarts() {
        final List<JobStart> jobStarts = new ArrayList<>();

		repository.findByIdCdmandantAndCdmeld(getCdMandant(), CdMeld.NEUEINTRERF.getCode()).forEach(ptVerm -> {
			jobStarts.add(buildJobStart(ptVerm));
		});
        return jobStarts;
    }

    private JobStart buildJobStart(TozsPtverm ptVerm) {
        return new JobStart(Integer.toString(ptVerm.getId().getId()), buildJobInfo(ptVerm),
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
