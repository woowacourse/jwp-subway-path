package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.LineStation;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class LineStationDaoTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineStationDao lineStationDao;

    @BeforeEach
    void setUp() {
        lineStationDao = new LineStationDao(jdbcTemplate, dataSource);
    }

    @DisplayName("노선에 역을 추가한다.")
    @Test
    void insert() {
        LineStation lineStation = new LineStation(3L, 2L, 3L, 6);
        LineStation insertedLineStation = lineStationDao.insert(lineStation);
        assertThat(insertedLineStation).isEqualTo(
                new LineStation(insertedLineStation.getId(),
                        lineStation.getUpBoundId(),
                        lineStation.getDownBoundId(),
                        lineStation.getLineId(),
                        lineStation.getDistance()));
    }

    @DisplayName("특정 라인에 해당하는 LineStation들을 조회한다.")
    @Test
    void findByLine() {
        LineStation firstLineStation = new LineStation(1L, 2L, 1L, 6);
        LineStation secondLineStation = new LineStation(2L, 3L, 1L, 6);
        LineStation thirdLineStation = new LineStation(3L, 4L, 1L, 6);

        LineStation first = lineStationDao.insert(firstLineStation);
        LineStation second = lineStationDao.insert(secondLineStation);
        LineStation third = lineStationDao.insert(thirdLineStation);

        List<LineStation> lineStations = lineStationDao.findByLine(1L);
        assertThat(lineStations.containsAll(List.of(first, second, third))).isTrue();
    }

    @DisplayName("LineStation을 업데이트 한다.")
    @Test
    void update() {
        LineStation firstLineStation = new LineStation(1L, 2L, 1L, 6);
        LineStation insertedStation = lineStationDao.insert(firstLineStation);
        LineStation updateStation = new LineStation(insertedStation.getId(), 1L, 3L, 1L, 6);

        lineStationDao.update(updateStation);
        List<LineStation> lineStations = lineStationDao.findByLine(1L);

        assertThat(lineStations.get(0)).isEqualTo(updateStation);
    }

    @DisplayName("Id로 LineStation을 삭제한다.")
    @Test
    void deleteById() {
        LineStation firstLineStation = new LineStation(1L, 2L, 1L, 6);
        LineStation insertedStation = lineStationDao.insert(firstLineStation);

        lineStationDao.deleteById(insertedStation.getId());

        assertThat(lineStationDao.findByLine(1L)).isEmpty();
    }
}
