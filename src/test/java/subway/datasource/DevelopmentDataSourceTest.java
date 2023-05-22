package subway.datasource;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("development")
public class DevelopmentDataSourceTest {
    
    @Autowired
    private DataSource dataSource;
    
    @Test
    @DisplayName("개발 환경에서 DataSource가 H2로 정상적으로 주입되는지 확인")
    void datasourceWithJavaConfig() {
        final HikariDataSource dataSource = (HikariDataSource) this.dataSource;
        Assertions.assertThat(dataSource.getJdbcUrl()).isEqualTo("jdbc:h2:mem:test");
    }
}
