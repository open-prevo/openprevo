/*============================================================================*
 * Copyright (c) 2018 - Prevo-System AG and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 3
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR GPL-3.0 WITH Classpath-exception-2.0
 * 
 * Contributors:
 *     Prevo-System AG - initial API and implementation
 *===========================================================================*/
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
