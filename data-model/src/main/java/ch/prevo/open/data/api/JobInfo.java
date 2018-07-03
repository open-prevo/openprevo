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

	public void setRetirementFundUid(String retirementFundUid) {
		this.retirementFundUid = retirementFundUid;
	}
	
	public String getInternalReferenz() {
		return internalReferenz;
	}

	public void setInternalReferenz(String internalReferenz) {
		this.internalReferenz = internalReferenz;
	}

	public String getOasiNumber() {
		return oasiNumber;
	}

	public void setOasiNumber(String oasiNumber) {
		this.oasiNumber = oasiNumber;
	}

	public String getInternalPersonId() {
		return internalPersonId;
	}

	public void setInternalPersonId(String internalPersonId) {
		this.internalPersonId = internalPersonId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
}
