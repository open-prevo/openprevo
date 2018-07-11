package ch.prevo.pakt.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the TOZS_PTVERM database table.
 * 
 */
@Embeddable
public class TozsPtvermPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private int id;

	private short cdmandant;

	public TozsPtvermPK() {
	}
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public short getCdmandant() {
		return this.cdmandant;
	}
	public void setCdmandant(short cdmandant) {
		this.cdmandant = cdmandant;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TozsPtvermPK)) {
			return false;
		}
		TozsPtvermPK castOther = (TozsPtvermPK)other;
		return 
			(this.id == castOther.id)
			&& (this.cdmandant == castOther.cdmandant);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.id;
		hash = hash * prime + ((int) this.cdmandant);
		
		return hash;
	}
}