package subway.yml;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles({"second", "test"})
public class ProfileActiveSecondTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void test_default1() throws Exception {
        String expectedDatabaseUrl = "jdbc:h2:mem:second";
        String actualDatabaseUrl = dataSource.getConnection().getMetaData().getURL();

        assertEquals(expectedDatabaseUrl, actualDatabaseUrl);
    }
}