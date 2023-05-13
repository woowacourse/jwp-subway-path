package subway.dao;

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class LineDaoTest {

    Long lineId;
    private LineDao lineDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate, dataSource);

        lineId = 1L;
    }

    @Test
    @DisplayName("")
    void isUpEndStation_true() {
        // given
        String upEndStationName = "강남역";

        // when
        boolean actual = lineDao.isUpEndStation(lineId, upEndStationName);

        // then
        Assertions.assertThat(actual).isTrue();

    }

    @ParameterizedTest
    @ValueSource(strings={"역삼역", "잠실역"})
    @DisplayName("")
    void isUpEndStation_false(String remainStationName) {
        // given, when
        boolean actual = lineDao.isUpEndStation(lineId, remainStationName);

        // then
        Assertions.assertThat(actual).isFalse();

    }
}