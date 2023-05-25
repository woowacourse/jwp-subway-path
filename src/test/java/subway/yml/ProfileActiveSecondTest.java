package subway.yml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import subway.route.application.JgraphtRouteFinder;

@SpringBootTest
@ActiveProfiles({"second", "test"})
class ProfileActiveSecondTest {

  @Autowired
  private DataSource dataSource;

  @MockBean
  private JgraphtRouteFinder jgraphtRouteFinder;

  @Test
  void test_default1() throws Exception {
    String expectedDatabaseUrl = "jdbc:h2:mem:second";
    String actualDatabaseUrl = dataSource.getConnection().getMetaData().getURL();

    assertEquals(expectedDatabaseUrl, actualDatabaseUrl);
  }
}
