package ch.prevo.pakt;
public enum RetirementFund {
	BALOISE_SAMMELSTIFTUNG(Short.valueOf("4"), 
			"Baloise-Sammelstiftung fuer die obligatorische berufliche Vorsorge",
			"CHE-109.740.084-Perspectiva Sammelstiftung fuer berufliche Vorsorge",
			""), 
	PERSPECTIVA_SAMMELSTIFTUNG(Short.valueOf("1"),
			"Perspectiva Sammelstiftung fuer berufliche Vorsorge", 
			"CHE-223.471.073-Perspectiva Sammelstiftung fuer berufliche Vorsorge",
			""),
	HELVETIA_SAMMELSTIFTUNG(Short.valueOf("0"),
			"Helvetia-Sammelstiftung",
			"CHE-109.537.519-Helvetia-Sammelstiftung", 
			"60000201");
	private short cdStf;
	private String name;
	private String id;
	private String idPartner;

	private RetirementFund(Short cdStf, String name, String id, String idPartner) {
		this.cdStf = cdStf;
		this.name = name;
		this.id = id;
		this.idPartner = idPartner;
	}

	public Short getCdStf() {
		return cdStf;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}
	
	public String getIdPartner() {
		return idPartner;
	}

	public static RetirementFund getByCdStf(short cdStf) {
		RetirementFund result = null;
		for (RetirementFund candidate : RetirementFund.values()) {
			if (candidate.cdStf == cdStf) {
				result = candidate;
				break;
			}
		}
		return result;
	}
	
	public static RetirementFund getById(String id) {
		RetirementFund result = null;
		for (RetirementFund candidate : RetirementFund.values()) {
			if (candidate.id.equals(id)) {
				result = candidate;
				break;
			}
		}
		return result;
	}
}