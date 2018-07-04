package ch.prevo.open.data.api;

import java.time.LocalDate;

public class JobInfo {
	
	private String retirementFundUid;
	
	private String internalReferenz;
	
	private String oasiNumber;
	
	private String internalPersonId;

	private LocalDate date;

	public String getRetirementFundUid() {
		return retirementFundUid;
	}

	public JobInfo setRetirementFundUid(String retirementFundUid) {
		this.retirementFundUid = retirementFundUid;
		return this;
	}
	
	public String getInternalReferenz() {
		return internalReferenz;
	}

	public JobInfo setInternalReferenz(String internalReferenz) {
		this.internalReferenz = internalReferenz;
		return this;
	}

	public String getOasiNumber() {
		return oasiNumber;
	}

	public JobInfo setOasiNumber(String oasiNumber) {
		this.oasiNumber = oasiNumber;
		return this;
	}

	public String getInternalPersonId() {
		return internalPersonId;
	}

	public JobInfo setInternalPersonId(String internalPersonId) {
		this.internalPersonId = internalPersonId;
		return this;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
}
