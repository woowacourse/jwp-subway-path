package subway.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;
import subway.entity.LineStationEntity;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = LineStationDao.class)
public class LineStationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private LineStationDao lineStationDao;

    @DisplayName("LineStation 추가한다.")
    @Test
    public void insert_lineStation_with_id() {
        // given
        LineStationEntity lineStation = new LineStationEntity(null, 1L, 1L);

        // when
        LineStationEntity insertedLineStation = lineStationDao.insert(lineStation);

        // then
        assertThat(insertedLineStation.getId()).isNotNull();
        assertThat(insertedLineStation.getStationId()).isEqualTo(1L);
        assertThat(insertedLineStation.getLineId()).isEqualTo(1L);
    }

    @DisplayName("lineId로 LINE_STATION을 제거한다.")
    @Test
    public void delete_by_line_id() {
        // given
        LineStationEntity lineStation1 = new LineStationEntity(null, 1L, 1L);
        LineStationEntity lineStation2 = new LineStationEntity(null, 2L, 1L);
        lineStationDao.insert(lineStation1);
        lineStationDao.insert(lineStation2);

        // when
        lineStationDao.deleteByLineId(1L);

        // then
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "LINE_STATION", "LINE_ID = 1")).isEqualTo(0);
    }

    @DisplayName("line_id와 station_id로 제거한다.")
    @Test
    public void delete_by_line_id_and_station_id() {
        // given
        LineStationEntity lineStation1 = new LineStationEntity(null, 1L, 1L);
        LineStationEntity lineStation2 = new LineStationEntity(null, 2L, 1L);
        LineStationEntity lineStation3 = new LineStationEntity(null, 1L, 2L);
        lineStationDao.insert(lineStation1);
        lineStationDao.insert(lineStation2);
        lineStationDao.insert(lineStation3);

        // when
        lineStationDao.deleteByLineIdAndStationId(1L, 1L);

        // then
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "LINE_STATION", "LINE_ID = 1 AND STATION_ID = 1")).isEqualTo(0);
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "LINE_STATION", "LINE_ID = 2 AND STATION_ID = 1")).isEqualTo(1);
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "LINE_STATION", "LINE_ID = 1 AND STATION_ID = 2")).isEqualTo(1);
    }
}
