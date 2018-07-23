package ch.prevo.pakt.soap;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import ch.prevo.pakt.utils.SoapUtil;

@Service
public class SoapRequest {

	private static Logger LOG = LoggerFactory.getLogger(SoapRequest.class);

	private final SoapUtil soapUtil = new SoapUtil();
	
	@Inject
	private ResourceLoader loader;

	@Value("${pakt.service.template.file.submitFzlVerwendung}")
	private String submitFzlVerwendungFile;

	private String submitFzlVerwendungTemplate;

	@PostConstruct
	public void init() {
		submitFzlVerwendungTemplate = initTemplate(submitFzlVerwendungFile);
	}

	private String initTemplate(String file) {
		String template = "";
		try {
			template = IOUtils.toString(loader.getResource(file).getInputStream(), Charset.defaultCharset());
		} catch (IOException e) {
			LOG.warn("Unable to read template from " + file, e);
		}
		return template;
	}

	public void callSubmitFZLVerwendung(String serviceBaseUrl,
			SubmitFZLVerwendungRequestPopulator soapRequestPopulator, Consumer<Document> responseConsumer)
			throws IOException {
		getSoapUtil().callSoapRequest(serviceBaseUrl + "MeldungUpdate?wsdl",
				"http://generate.extern.update.meldung.service.prevo.ch/submitFZLVerwendung", getSubmitFzlVerwendungTemplate(),
				soapRequestPopulator, responseConsumer);
	}

	private SoapUtil getSoapUtil() {
		return soapUtil;
	}

	private String getSubmitFzlVerwendungTemplate() {
		return submitFzlVerwendungTemplate;
	}
}
