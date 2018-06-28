package ch.prevo.open.node.pakt;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the TOZS_PTVERM database table.
 * 
 */
@Entity
@Table(name="TOZS_PTVERM")
@NamedQuery(name="TozsPtverm.findAll", query="SELECT t FROM TozsPtverm t")
public class TozsPtverm implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TozsPtvermPK id;

	private String ahv;

	private short cdmeld;

	private short cdsex;

	private short cdst;

	private short cdstf;

	@Temporal(TemporalType.DATE)
	private Date dtgeb;

	@Temporal(TemporalType.DATE)
	private Date dtgueltab;

	private String idgeschaeftpol;

	private String idgeschaeftvtr;

	private String name;

	private String namevor;

	private int nrgevo;

	private Timestamp timestamp;

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

	public Date getDtgeb() {
		return this.dtgeb;
	}

	public void setDtgeb(Date dtgeb) {
		this.dtgeb = dtgeb;
	}

	public Date getDtgueltab() {
		return this.dtgueltab;
	}

	public void setDtgueltab(Date dtgueltab) {
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

	public String getNamevor() {
		return this.namevor;
	}

	public void setNamevor(String namevor) {
		this.namevor = namevor;
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

}