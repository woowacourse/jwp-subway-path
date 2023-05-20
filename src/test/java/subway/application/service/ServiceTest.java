package subway.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;


@Sql("/test.sql")
@Sql(value = "/drop.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest
public class ServiceTest {

    @Autowired
    protected PathService pathService;

    @Autowired
    protected LineService lineService;

    @Autowired
    protected FeeService feeService;

    @Autowired
    protected StationService stationService;
}