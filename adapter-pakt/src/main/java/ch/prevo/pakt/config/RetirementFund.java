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

public class RetirementFund {
	private String uid;
	private String name;
	private String idPartner;
	private short cdStf;

	public RetirementFund() {
		super();
	}

	public RetirementFund(String uid, String name, String idPartner, short cdStf) {
		super();
		this.uid = uid;
		this.name = name;
		this.idPartner = idPartner;
		this.cdStf = cdStf;
	}

	@Override
	public String toString() {
		return "RetirementFund [uid=" + uid + ", name=" + name + ", idPartner=" + idPartner + ", cdStf=" + cdStf + "]";
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdPartner() {
		return idPartner;
	}

	public void setIdPartner(String idPartner) {
		this.idPartner = idPartner;
	}

	public short getCdStf() {
		return cdStf;
	}

	public void setCdStf(short cdStf) {
		this.cdStf = cdStf;
	}
}
