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
package ch.prevo.pakt.soap;

import java.time.LocalDate;
import java.util.function.Function;

public class SubmitFZLVerwendungRequestPopulator implements Function<String, String> {

	private final Short cdMandant;

	private final String idUser;

	private final String idPartner;

	private final String idGeschaeftPol;

	private final LocalDate dtUnterVoll;

	private final String txtUebw;

	public SubmitFZLVerwendungRequestPopulator(Short cdMandant, String idUser, String idGeschaeftPol, String idPartner,
			LocalDate dtUnterVoll, String txtUebw) {
		super();
		this.cdMandant = cdMandant;
		this.idUser = idUser;
		this.idGeschaeftPol = idGeschaeftPol;
		this.idPartner = idPartner;
		this.dtUnterVoll = dtUnterVoll;
		this.txtUebw = txtUebw;
	}

	@Override
	public String apply(String soapRequest) {
		return String.format(soapRequest, "" + cdMandant, idUser, idGeschaeftPol, idPartner, dtUnterVoll, txtUebw);
	}
}
