package ch.prevo.pakt.utils;

import java.io.IOException;
import java.util.function.Consumer;

import org.w3c.dom.Document;

public class SoapRequest {

	public final static String SUBMIT_FZL_VERWENDUNG = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:gen=\"http://generate.extern.update.meldung.service.prevo.ch\">\r\n"
			+ "    <soapenv:Header/>\r\n" + "    <soapenv:Body>\r\n" + "       <gen:submitFZLVerwendung>\r\n"
			+ "          <core>\r\n" + "             <cdMandant>%s</cdMandant>\r\n"
			+ "             <cdSchicht>1</cdSchicht>\r\n" + "             <cdSprache>1</cdSprache>\r\n"
			+ "             <cdProfil>1</cdProfil>\r\n" + "             <idUser>%s</idUser>\r\n"
			+ "          </core>\r\n" + "          <idGeschaeft>%s</idGeschaeft>\r\n"
			+ "          <nrMeld>0</nrMeld>\r\n" + "          <slFZLDatenChecked>true</slFZLDatenChecked>\r\n"
			+ "          <fzlVerwendungen>\r\n" + "             <!--1 to 2 repetitions:-->\r\n"
			+ "             <fzlVerwendung>\r\n" + "                <idPartner>%s</idPartner>\r\n"
			+ "                <cdZweck>1</cdZweck>\r\n" + "                <idGeschaeft></idGeschaeft>\r\n"
			+ "                <slPartnerChecked>true</slPartnerChecked>\r\n" + "                <cdWhg>1</cdWhg>\r\n"
			+ "                <cdGrdAusz>0</cdGrdAusz>\r\n" + "                <dtUnterVoll>%s</dtUnterVoll>\r\n"
			+ "                <txtUebw>%s</txtUebw>\r\n" + "                <cdAntFzl>3</cdAntFzl>\r\n"
			+ "             </fzlVerwendung>\r\n" + "          </fzlVerwendungen>\r\n"
			+ "          <!--Optional:-->\r\n" + "          <idAuftrag></idAuftrag>\r\n"
			+ "       </gen:submitFZLVerwendung>\r\n" + "    </soapenv:Body>\r\n" + " </soapenv:Envelope>";

	public static void callSubmitFZLVerwendung(String serviceBaseUrl,
			SubmitFZLVerwendungRequestPopulator soapRequestPopulator, Consumer<Document> responseConsumer)
			throws IOException {
		getSoapUtil().callSoapRequest(serviceBaseUrl + "MeldungUpdate?wsdl", "http://generate.extern.update.meldung.service.prevo.ch/submitFZLVerwendung", SUBMIT_FZL_VERWENDUNG,
				soapRequestPopulator, responseConsumer);
	}

	private static SoapUtil getSoapUtil() {
		return new SoapUtil();
	}
}
