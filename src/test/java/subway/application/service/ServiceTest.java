package subway.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test.sql")
@SpringBootTest
public class ServiceTest {

    @Autowired
    protected PathService pathService;
}
