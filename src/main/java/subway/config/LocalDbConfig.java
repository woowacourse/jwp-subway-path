package subway.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("local")
@Configuration
public class LocalDbConfig {

    @Bean
    public DataSource dataSource(
            @Value("${spring.datasource.username}") String userName,
            @Value("${spring.datasource.password}") String password
    ) {

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(
                "jdbc:mysql://localhost:3306/subwaymap?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul&characterEncoding=UTF-8");
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        return dataSource;
    }
}
