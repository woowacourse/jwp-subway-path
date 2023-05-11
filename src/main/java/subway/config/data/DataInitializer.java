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
        jdbcTemplate.update("INSERT INTO lines(name, color) VALUES('1', '파랑')");
        jdbcTemplate.update("INSERT INTO lines(name, color) VALUES('2', '초록')");
        jdbcTemplate.update("INSERT INTO stations(name) VALUES('루카')");
        jdbcTemplate.update("INSERT INTO stations(name) VALUES('헤나')");
        jdbcTemplate.update("INSERT INTO stations(name) VALUES('루카_헤나')");
        jdbcTemplate.update("INSERT INTO stations_lines(station_id, line_id) VALUES(1, 1)");
        jdbcTemplate.update("INSERT INTO stations_lines(station_id, line_id) VALUES(2, 1)");
        jdbcTemplate.update("INSERT INTO stations_lines(station_id, line_id) VALUES(1, 2)");
        jdbcTemplate.update("INSERT INTO stations_lines(station_id, line_id) VALUES(3, 2)");
        jdbcTemplate.update("INSERT INTO sections(distance, is_start, up_station_id, down_station_id, line_id)VALUES (10, true, 1, 2, 1)");
        jdbcTemplate.update("INSERT INTO sections(distance, is_start, up_station_id, down_station_id, line_id) VALUES (10, true, 3, 1, 2)");
    }
}
