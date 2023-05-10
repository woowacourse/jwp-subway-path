package subway.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineStationEntity;

@JdbcTest
class LineStationEntityDaoTest {

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
        LineStationEntity lineStationEntity = new LineStationEntity(1L, 1L);

        // when
        LineStationEntity result = lineStationDao.insert(lineStationEntity);

        // then
        assertThat(result.getStationId()).isEqualTo(lineStationEntity.getStationId());
        assertThat(result.getLineId()).isEqualTo(lineStationEntity.getLineId());
    }
}