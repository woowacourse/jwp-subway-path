package subway.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DataSourceTest {

    @Autowired
    private DataSource dataSource;

    @Test
    @DisplayName("테스트 전용 DB를 설정할 수 있다.")
    void test_datasource() throws Exception {
        //given

        final DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
        final String url = metaData.getURL();
        final String driverName = metaData.getDriverName();

        //when & then
        assertEquals("jdbc:h2:mem:testdb", url);
    }
}
