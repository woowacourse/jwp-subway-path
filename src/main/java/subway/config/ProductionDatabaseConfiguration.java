package subway.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@Configuration
public class ProductionDatabaseConfiguration {

    @Value("${my.datasource.url}")
    private String url;

    @Value("${my.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${my.datasource.username}")
    private String userName;

    @Value("${my.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url(url)
                .driverClassName(driverClassName)
                .username(userName)
                .password(password)
                .build();
    }
}
