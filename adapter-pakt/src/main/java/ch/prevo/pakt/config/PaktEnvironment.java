package ch.prevo.pakt.config;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "pakt")
@PropertySource("classpath:pakt.properties")
@Validated
public class PaktEnvironment {
	@NotNull
	private Short cdMandant;
	
	@NotEmpty
	private String idUser;
	
	@NotEmpty
	private String serviceBaseUrl;
	
	public Short getCdMandant() {
		return cdMandant;
	}

	public void setCdMandant(Short cdMandant) {
		this.cdMandant = cdMandant;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getServiceBaseUrl() {
		return serviceBaseUrl;
	}

	public void setServiceBaseUrl(String serviceBaseUrl) {
		this.serviceBaseUrl = serviceBaseUrl;
	}
}
