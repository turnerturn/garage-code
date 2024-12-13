package com.example.demo;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatasourceConfiguration {

    /**
     * Creates a DataSourceProperties bean configured with properties prefixed with
     * "app.datasource".
     * 
     * @return a DataSourceProperties object
     */

    @Bean
    @ConfigurationProperties("app.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * Creates a HikariDataSource bean configured with properties prefixed with
     * "app.datasource.configuration".
     * 
     * <p>
     * You can easily initialize a DataSourceBuilder from the state of any DataSourceProperties object.
     * This allows you to inject the DataSource that Spring Boot creates automatically.  
     * However, that would split your configuration into two namespaces: url,
     * username, password, type, and driver
     * on spring.datasource and the rest on your custom namespace (app.datasource).
     *
     * To avoid that, you can redefine a custom DataSourceProperties on your custom
     * namespace, as shown in the following example:
     * </p> 
     * @param properties the DataSourceProperties object
     * @return a HikariDataSource object
     */
    @Bean
    @ConfigurationProperties("app.datasource.configuration")
    public HikariDataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }

}
