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
