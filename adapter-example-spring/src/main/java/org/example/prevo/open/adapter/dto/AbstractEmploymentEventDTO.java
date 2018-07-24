/*******************************************************************************
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
 ******************************************************************************/
package org.example.prevo.open.adapter.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@MappedSuperclass
public class AbstractEmploymentEventDTO {

    @Id
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    private EmploymentInfoDTO employmentInfo;

    private String techId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTechId() {
        return techId;
    }

    public void setTechId(String techId) {
        this.techId = techId;
    }

    public EmploymentInfoDTO getEmploymentInfo() {
        return employmentInfo;
    }

    public void setEmploymentInfo(EmploymentInfoDTO employmentInfo) {
        this.employmentInfo = employmentInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("techId", techId)
                .append("employmentInfo", employmentInfo)
                .toString();
    }
}
