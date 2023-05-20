package subway;

import static org.assertj.core.api.Fail.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import org.junit.jupiter.api.Test;

public class DbConnectionTest {

    @Test
    public void mySqlConnectionTest() {
        try(Connection con =
                    DriverManager.getConnection(
                            "jdbc:mysql://localhost:13306/subway?serverTimezone=UTC&characterEncoding=UTF-8",
                            "root",
                            "root")){
            System.out.println(con);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
