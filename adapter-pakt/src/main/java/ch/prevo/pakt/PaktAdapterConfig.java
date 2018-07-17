package ch.prevo.pakt;

import javax.sql.DataSource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Configuration
@ComponentScan
@EnableJpaRepositories(entityManagerFactoryRef = "paktEntityManagerFactory", basePackages = {
		"ch.prevo.pakt.repository" })
@PropertySource("classpath:pakt-application.properties")
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "pakt")
@Validated
public class PaktAdapterConfig {

	@NotNull
	private Short cdMandant;
	
	@NotEmpty
	private String idUser;
	
	@NotEmpty
	private String serviceBaseUrl;

	@Bean(name = "paktDataSource")
	@ConfigurationProperties(prefix = "pakt.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "paktEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean paktEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("paktDataSource") DataSource dataSource) {
		return builder.dataSource(dataSource).packages("ch.prevo.pakt").persistenceUnit("pakt").build();
	}
	
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
