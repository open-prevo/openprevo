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
