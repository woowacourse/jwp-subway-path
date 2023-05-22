package subway.datasource;

import static org.assertj.core.api.Assertions.assertThat;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("production")
public class ProductionDataSourceTest {
    
    @Autowired
    private DataSource dataSource;
    
    @Test
    @DisplayName("프로덕션 환경에서 DataSource가 Mysql로 정상적으로 주입되는지 확인")
    void datasourceWithJavaConfig() {
        final HikariDataSource dataSource = (HikariDataSource) this.dataSource;
        final String url = dataSource.getJdbcUrl();
        assertThat(url).isEqualTo("jdbc:mysql://localhost:3306/subway");
        assertThat(dataSource.getUsername()).isEqualTo("root");
    }
    
}
