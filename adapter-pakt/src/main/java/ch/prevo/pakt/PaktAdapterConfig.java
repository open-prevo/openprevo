package ch.prevo.pakt;

import javax.sql.DataSource;

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

@Component
@Configuration
@ComponentScan
@EnableJpaRepositories(entityManagerFactoryRef = "paktEntityManagerFactory", basePackages = {
		"ch.prevo.pakt.repository" })
@PropertySource("classpath:pakt-application.properties")
@EnableAutoConfiguration
public class PaktAdapterConfig {

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
}
