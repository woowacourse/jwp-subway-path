package subway.config.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DataInitializer(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(final String... args) throws Exception {
        jdbcTemplate.update("INSERT INTO lines(name, color) VALUES('2호선', '초록')");
        jdbcTemplate.update("INSERT INTO lines(name, color) VALUES('8호선', '파랑')");
        jdbcTemplate.update("INSERT INTO stations(name) VALUES('잠실새내')");
        jdbcTemplate.update("INSERT INTO stations(name) VALUES('잠실')");
        jdbcTemplate.update("INSERT INTO stations(name) VALUES('잠실나루')");
        jdbcTemplate.update("INSERT INTO stations(name) VALUES('몽촌토성')");
        jdbcTemplate.update("INSERT INTO stations(name) VALUES('석촌')");
        jdbcTemplate.update("INSERT INTO stations(name) VALUES('더미')");
        jdbcTemplate.update("INSERT INTO sections(distance, up_station_id, down_station_id, line_id)VALUES (10, 1, 2, 1)");
        jdbcTemplate.update("INSERT INTO sections(distance, up_station_id, down_station_id, line_id) VALUES (15, 2, 3, 1)");
        jdbcTemplate.update("INSERT INTO sections(distance, up_station_id, down_station_id, line_id)VALUES (10, 4, 3, 2)");
        jdbcTemplate.update("INSERT INTO sections(distance, up_station_id, down_station_id, line_id) VALUES (15, 3, 5, 2)");
    }
}
