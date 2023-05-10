package subway.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import subway.domain.line.Line;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.Stations;
import subway.entity.LineStation;

@JdbcTest
class LineStationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    private LineStationDao lineStationDao;

    @BeforeEach
    void setUp() {
        lineStationDao = new LineStationDao(jdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("Line_Station 테이블 삽입")
    void line_station_insert() {
        // given
        LineStation lineStation = new LineStation(1L, 1L);

        // when
        LineStation result = lineStationDao.insert(lineStation);

        // then
        assertThat(result.getStationId()).isEqualTo(lineStation.getStationId());
        assertThat(result.getLineId()).isEqualTo(lineStation.getLineId());
    }
}