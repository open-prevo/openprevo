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
package ch.prevo.pakt.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * The persistent class for the TOZS_PTVERM database table.
 * 
 */
@Entity
@Table(name="TOZS_PTVERM")
public class TozsPtverm implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TozsPtvermPK id;

	private String ahv;

	private short cdmeld;

	private short cdsex;

	private short cdst;

	private short cdstf;

	private LocalDate dtgeb;

	private LocalDate dtgueltab;

	private String idgeschaeftpol;

	private String idgeschaeftvtr;

	private String name;

	private String namestrasse;

	private String nameve;

	private String namevor;

	private String namezusatz;

	private int nrgevo;

	private Timestamp timestamp;

	private String txtesr1;

	private String txtesr2;

	private String txtiban;

	private String txtort;

	private String txtpostfach;

	private String txtpostleitzahl;

	public TozsPtverm() {
	}

	public TozsPtvermPK getId() {
		return this.id;
	}

	public void setId(TozsPtvermPK id) {
		this.id = id;
	}

	public String getAhv() {
		return this.ahv;
	}

	public void setAhv(String ahv) {
		this.ahv = ahv;
	}

	public short getCdmeld() {
		return this.cdmeld;
	}

	public void setCdmeld(short cdmeld) {
		this.cdmeld = cdmeld;
	}

	public short getCdsex() {
		return this.cdsex;
	}

	public void setCdsex(short cdsex) {
		this.cdsex = cdsex;
	}

	public short getCdst() {
		return this.cdst;
	}

	public void setCdst(short cdst) {
		this.cdst = cdst;
	}

	public short getCdstf() {
		return this.cdstf;
	}

	public void setCdstf(short cdstf) {
		this.cdstf = cdstf;
	}

	public LocalDate getDtgeb() {
		return this.dtgeb;
	}

	public void setDtgeb(LocalDate dtgeb) {
		this.dtgeb = dtgeb;
	}

	public LocalDate getDtgueltab() {
		return this.dtgueltab;
	}

	public void setDtgueltab(LocalDate dtgueltab) {
		this.dtgueltab = dtgueltab;
	}

	public String getIdgeschaeftpol() {
		return this.idgeschaeftpol;
	}

	public void setIdgeschaeftpol(String idgeschaeftpol) {
		this.idgeschaeftpol = idgeschaeftpol;
	}

	public String getIdgeschaeftvtr() {
		return this.idgeschaeftvtr;
	}

	public void setIdgeschaeftvtr(String idgeschaeftvtr) {
		this.idgeschaeftvtr = idgeschaeftvtr;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamestrasse() {
		return this.namestrasse;
	}

	public void setNamestrasse(String namestrasse) {
		this.namestrasse = namestrasse;
	}

	public String getNameve() {
		return this.nameve;
	}

	public void setNameve(String nameve) {
		this.nameve = nameve;
	}

	public String getNamevor() {
		return this.namevor;
	}

	public void setNamevor(String namevor) {
		this.namevor = namevor;
	}

	public String getNamezusatz() {
		return this.namezusatz;
	}

	public void setNamezusatz(String namezusatz) {
		this.namezusatz = namezusatz;
	}

	public int getNrgevo() {
		return this.nrgevo;
	}

	public void setNrgevo(int nrgevo) {
		this.nrgevo = nrgevo;
	}

	public Timestamp getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getTxtesr1() {
		return this.txtesr1;
	}

	public void setTxtesr1(String txtesr1) {
		this.txtesr1 = txtesr1;
	}

	public String getTxtesr2() {
		return this.txtesr2;
	}

	public void setTxtesr2(String txtesr2) {
		this.txtesr2 = txtesr2;
	}

	public String getTxtiban() {
		return this.txtiban;
	}

	public void setTxtiban(String txtiban) {
		this.txtiban = txtiban;
	}

	public String getTxtort() {
		return this.txtort;
	}

	public void setTxtort(String txtort) {
		this.txtort = txtort;
	}

	public String getTxtpostfach() {
		return this.txtpostfach;
	}

	public void setTxtpostfach(String txtpostfach) {
		this.txtpostfach = txtpostfach;
	}

	public String getTxtpostleitzahl() {
		return this.txtpostleitzahl;
	}

	public void setTxtpostleitzahl(String txtpostleitzahl) {
		this.txtpostleitzahl = txtpostleitzahl;
	}

}
