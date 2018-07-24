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
package ch.prevo.pakt.zd.utils;

public enum CdMeld {

	DADURCHF(Short.parseShort("4"), "Dienstaustritt"), NEUEINTRERF(Short.parseShort("6"), "Neueintritt");

	private String key;
	private Short code;
	private String text;

	private CdMeld(short code, String text) {
		this.key = String.valueOf(code);
		this.code = code;
		this.text = text;
	}

	public short getCode() {
		return code;
	}

	public String getText() {
		return text;
	}

	public static CdMeld getByCode(short code) {
		CdMeld result = null;
		for (CdMeld candidate : CdMeld.values()) {
			if (candidate.code == code) {
				result = candidate;
				break;
			}
		}
		return result;
	}

	public String getKey() {
		return key;
	}

}
