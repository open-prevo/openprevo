package ch.prevo.pakt.utils;

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
