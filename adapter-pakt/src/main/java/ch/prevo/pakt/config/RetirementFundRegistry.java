package ch.prevo.pakt.config;

import java.util.List;
import java.util.Objects;

public interface RetirementFundRegistry {
	/**
	 * Provide all currently registered retirement funds.
	 *
	 * @return The configuration for each retirement fund.
	 */
	List<RetirementFund> getCurrentRetirementFunds();

	public default RetirementFund getByCdStf(short cdStf) {
		return cdStf > 0
				? getCurrentRetirementFunds().stream()
						.filter(retirementFund -> retirementFund.getCdStf() > 0 && retirementFund.getCdStf() == cdStf)
						.findFirst().orElse(null)
				: null;
	}

	public default RetirementFund getByUid(String uid) {
		return Objects.nonNull(uid) 
				? getCurrentRetirementFunds().stream()
						.filter(retirementFund -> uid.equals(retirementFund.getUid())).findFirst()
						.orElse(null)
				: null;
	}
}
