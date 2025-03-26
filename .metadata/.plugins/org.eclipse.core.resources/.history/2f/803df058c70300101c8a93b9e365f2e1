package com.example.ipldream.dreamipl.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.example.ipldream.dreamipl.db3.repository",
    entityManagerFactoryRef = "db3EntityManagerFactory",
    transactionManagerRef = "db3TransactionManager"
)
public class DB3Configuration {
	
	@Bean(name="db3datasource")
	 @ConfigurationProperties(prefix = "spring.datasource.db3")
	public DataSource db3datasource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name="db3EntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean bean(@Qualifier("db3datasource") DataSource dataSource) {
		
		 HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	      vendorAdapter.setGenerateDdl(true);
	      
	      LocalContainerEntityManagerFactoryBean local=new LocalContainerEntityManagerFactoryBean();
	      local.setDataSource(dataSource);
	      local.setPackagesToScan("com.example.ipldream.dreamipl.db3.entity");
	      local.setJpaVendorAdapter(vendorAdapter);
	      local.setPersistenceUnitName("db3");
	      
	      return local;
	      
	}
	
	@Bean(name="db3transction")
	public PlatformTransactionManager manager(@Qualifier("db3EntityManagerFactory") EntityManagerFactory entityman) {
		return new JpaTransactionManager(entityman);
	}

}
