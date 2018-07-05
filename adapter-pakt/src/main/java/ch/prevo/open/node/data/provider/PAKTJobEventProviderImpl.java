package ch.prevo.open.node.data.provider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import ch.prevo.open.data.api.Address;
import ch.prevo.open.data.api.CapitalTransferInformation;
import ch.prevo.open.data.api.JobEnd;
import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.data.api.JobStart;
import ch.prevo.pakt.PartnerVermittlungRepository;
import ch.prevo.pakt.TozsPtverm;
import ch.prevo.pakt.zd.utils.CdMeld;

@Service
public class PAKTJobEventProviderImpl implements JobEndProvider, JobStartProvider {

	@Inject
	private PartnerVermittlungRepository repository;

	@Override
	public List<JobEnd> getJobEnds() {
		final List<JobEnd> jobEnds = new ArrayList<>();

		repository.findAll().forEach(ptVerm -> {
			if (CdMeld.DADURCHF.getCode() == ptVerm.getCdmeld()) {
				jobEnds.add(buildJobEnd(ptVerm));
			}
		});
		return jobEnds;
	}

	private JobEnd buildJobEnd(TozsPtverm ptVerm) {
		// TODO fulfill missing properties
		return new JobEnd(Integer.toString(ptVerm.getId().getId()), buildJobInfo(ptVerm));
	}

	private JobInfo buildJobInfo(TozsPtverm ptVerm) {
		JobInfo jobInfo = new JobInfo();
		jobInfo.setOasiNumber(ptVerm.getAhv());
		jobInfo.setRetirementFundUid(getRetirementFundId(ptVerm));
		jobInfo.setInternalPersonId(ptVerm.getIdgeschaeftpol());
		jobInfo.setInternalReferenz(ptVerm.getNameve());
		// TODO fulfill missing properties
		return jobInfo;

	}

	private String getRetirementFundId(TozsPtverm ptVerm) {
		return RetirementFund.getByCdStf(ptVerm.getCdstf()).getId();
	}

	enum RetirementFund {
		BALOISE_SAMMELSTIFTUNG(Short.valueOf("4"), "Baloise-Sammelstiftung für die obligatorische berufliche Vorsorge",
				"CHE-109.740.084"), PERSPECTIVA_SAMMELSTIFTUNG(Short.valueOf("1"),
						"Perspectiva Sammelstiftung für berufliche Vorsorge", "CHE-223.471.073");
		private Short cdStf;
		private String name;
		private String id;

		private RetirementFund(Short cdStf, String name, String id) {
			this.cdStf = cdStf;
			this.name = name;
			this.id = id;
		}

		public Short getCdStf() {
			return cdStf;
		}

		public void setCdStf(Short cdStf) {
			this.cdStf = cdStf;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public static RetirementFund getByCdStf(short cdStf) {
			RetirementFund result = null;
			for (RetirementFund candidate : RetirementFund.values()) {
				if (candidate.cdStf == cdStf) {
					result = candidate;
					break;
				}
			}
			return result;
		}
	}

	@Override
	public List<JobStart> getJobStarts() {
		final List<JobStart> jobStarts = new ArrayList<>();

		repository.findAll().forEach(ptVerm -> {
			if (CdMeld.NEUEINTRERF.getCode() == ptVerm.getCdmeld()) {
				jobStarts.add(buildJobStart(ptVerm));
			}
		});
		return jobStarts;
	}

	private JobStart buildJobStart(TozsPtverm ptVerm) {
		// TODO fulfill missing properties
		return new JobStart(Integer.toString(ptVerm.getId().getId()), buildJobInfo(ptVerm),
				buildCapitalTransferInformation(ptVerm));
	}

	private CapitalTransferInformation buildCapitalTransferInformation(TozsPtverm ptVerm) {
		// TODO Auto-generated method stub
		CapitalTransferInformation capitalTransferInfo = new CapitalTransferInformation(ptVerm.getNameve(),
				ptVerm.getTxtiban());
		capitalTransferInfo.setAddress(buildAddress(ptVerm));
		return capitalTransferInfo;
	}

	private Address buildAddress(TozsPtverm ptVerm) {
		// TODO Auto-generated method stub
		Address address = new Address();
		return address;
	}
}
