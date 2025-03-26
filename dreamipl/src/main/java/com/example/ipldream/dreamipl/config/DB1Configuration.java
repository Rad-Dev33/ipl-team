package com.example.ipldream.dreamipl.config;

import javax.sql.DataSource;


import javax.sql.DataSource;

import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
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
    basePackages = "com.example.ipldream.dreamipl.db1.repository",
    entityManagerFactoryRef = "db1EntityManagerFactory",
    transactionManagerRef = "db1TransactionManager"
)
public class DB1Configuration {
	
	
	//here we are creating a datasource for each database because we have multiple databases where as in case of single database spring automatically creates a datasource
	//configurationProperty search for prefix name spring.datasource.db1 in properties file configures the database name,passwor,url to a particular datasource and return the datasource object
	@Bean(name = "db1DataSource") //object name
    @ConfigurationProperties(prefix = "spring.datasource.db1")
    public DataSource db1DataSource() {
        return DataSourceBuilder.create().build();
    }
	
	
	//passing the datasource to an entity manager and return the entity maneger bean 
	 @Bean(name = "db1EntityManagerFactory")
	    public LocalContainerEntityManagerFactoryBean db1EntityManagerFactory(
	            @Qualifier("db1DataSource") DataSource dataSource) {

	        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	        vendorAdapter.setGenerateDdl(true);

	        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
	        factoryBean.setDataSource(dataSource);
	        factoryBean.setPackagesToScan("com.example.ipldream.dreamipl.db1.entity"); // Adjust package as needed
	        factoryBean.setJpaVendorAdapter(vendorAdapter);
	        factoryBean.setPersistenceUnitName("db1");

	        return factoryBean;
	    }
	 

	 // pass the entitymanager to entitytransction 
	    @Bean(name = "db1TransactionManager")
	    public PlatformTransactionManager db1TransactionManager(
	            @Qualifier("db1EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
	        return new JpaTransactionManager(entityManagerFactory);
	    }
	

}
