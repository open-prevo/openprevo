package ch.prevo.open.node.data.provider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import ch.prevo.open.data.api.JobEnd;
import ch.prevo.open.data.api.JobInfo;
import ch.prevo.open.data.api.JobStart;
import ch.prevo.open.node.data.provider.JobEndProvider;
import ch.prevo.open.node.data.provider.JobStartProvider;
import ch.prevo.open.node.pakt.PartnerVermittlungRepository;
import ch.prevo.open.node.pakt.TozsPtverm;
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
		return new JobEnd(Integer.toString(ptVerm.getId().getId()),
				new JobInfo().setOasiNumber(ptVerm.getAhv()).setRetirementFundUid(getRetirementFundId(ptVerm)));
	}

	private String getRetirementFundId(TozsPtverm ptVerm) {
		// TODO retrieve uid
		return Short.toString(ptVerm.getCdstf());
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
		return new JobStart(Integer.toString(ptVerm.getId().getId()),
				new JobInfo().setOasiNumber(ptVerm.getAhv()).setRetirementFundUid(getRetirementFundId(ptVerm)));

	}
}
