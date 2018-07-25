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
package ch.prevo.open.data.api;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class EmploymentCommencement extends AbstractEmployment {

    @Valid
    @NotNull
    private CapitalTransferInformation capitalTransferInfo;

    public EmploymentCommencement() {}

    public EmploymentCommencement(EmploymentInfo employmentInfo, CapitalTransferInformation capitalTransferInfo) {
		super(employmentInfo);
		this.capitalTransferInfo = capitalTransferInfo;
	}

    public CapitalTransferInformation getCapitalTransferInfo() {
        return capitalTransferInfo;
    }

    public void setCapitalTransferInfo(CapitalTransferInformation capitalTransferInfo) {
        this.capitalTransferInfo = capitalTransferInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EmploymentCommencement employmentCommencement = (EmploymentCommencement) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(capitalTransferInfo, employmentCommencement.capitalTransferInfo)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(capitalTransferInfo)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("capitalTransferInfo", capitalTransferInfo)
                .toString();
    }
}
