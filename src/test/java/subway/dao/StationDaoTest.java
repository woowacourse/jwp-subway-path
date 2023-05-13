package subway.dao;

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.StationEntity;

@JdbcTest
class StationDaoTest {

    Long lineId;
    private StationDao stationDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate, dataSource);

        lineId = 1L;
    }

    @Test
    @DisplayName("findHeadStationByLineId()를 호출하면 노선의 상행 종점 역을 반환한다")
    void findHeadStationByLineId() {
        // given
        String expected = "강남역";

        // when
        StationEntity entity = stationDao.findHeadStationByLineId(lineId);
        String actual = entity.getName();

        // then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

}