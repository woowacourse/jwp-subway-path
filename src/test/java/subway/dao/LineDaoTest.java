package subway.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineEntity;

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
    @DisplayName("insert()를 호출할 때 유효한 LineEntity를 입력하면 정상적으로 노선이 추가된다.")
    void insert_success() {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("findAll()를 호출하면 모든 노선의 정보를 반환한다")
    void findAll() {
        // given, when
        List<LineEntity> lines = lineDao.findAll();

        // then
        assertAll(
            () -> Assertions.assertThat(lines.get(0).getName()).isEqualTo("1호선")
        );
    }

    @ParameterizedTest
    @CsvSource(value = {"1:1호선"}, delimiter = ':')
    @DisplayName("findById()를 호출하면 입력한 id를 갖는 노선의 정보를 반환한다.")
    void findById(Long lineId, String expected) {
        // given, when
        LineEntity line = lineDao.findById(lineId);
        String actual = line.getName();
        // then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("isUpEndStation()를 호출할 때 입력받은 이름의 역이 노선의 상행종점이라면 true를 반환한다")
    void isUpEndStation_true() {
        // given
        String upEndStationName = "강남역";

        // when
        boolean actual = lineDao.isUpEndStation(lineId, upEndStationName);

        // then
        Assertions.assertThat(actual).isTrue();

    }

    @ParameterizedTest
    @ValueSource(strings = {"역삼역", "잠실역", "선릉역", "건대입구역", "삼성역", "없는역"})
    @DisplayName("isUpEndStation()를 호출할 때 입력받은 이름의 역이 노선의 상행종점이 아니라면 false를 반환한다")
    void isUpEndStation_false(String remainStationName) {
        // given, when
        boolean actual = lineDao.isUpEndStation(lineId, remainStationName);

        // then
        Assertions.assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("updateHeadStation()를 호출하면 입력받은 노선의 head_station 값을 변경한다")
    void updateHeadStation() {
        // given
        Long newHeadStation = 4L;
        Long before = lineDao.findById(lineId).getHeadStation();

        // when
        lineDao.updateHeadStation(lineId, newHeadStation);
        Long after = lineDao.findById(lineId).getHeadStation();

        // then
        assertAll(
            () -> Assertions.assertThat(before).isNotEqualTo(after),
            () -> Assertions.assertThat(after).isEqualTo(newHeadStation)
        );
    }

    @Test
    @DisplayName("deleteById()를 호출하면 입력받은 노선을 제거한다.")
    void deleteById() {
        // given, when
        lineDao.deleteById(lineId);

        // then
        Assertions.assertThatThrownBy(() -> lineDao.findById(lineId))
            .isInstanceOf(EmptyResultDataAccessException.class);
    }
}