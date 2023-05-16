package subway;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Profile("tset")
@Configuration
public class TestConfiguration {

    @Value("${TEST_DB_URL}")
    private String url;

    @Value("${TEST_DB_USERNAME}")
    private String userName;

    @Value("${TEST_DB_PASSWORD}")
    private String password;

    @Value("${TEST_DRIVER_CLASS_NAME}")
    private String driverClassName;

    @Bean
    public DataSource dataSourceConfiguration() {
        return DataSourceBuilder.create()
                .url(url)
                .username(userName)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }
}
