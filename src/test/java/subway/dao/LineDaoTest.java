package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.entity.LineEntity;

@Sql({"/schema-test.sql", "/data-test.sql"})
@JdbcTest
class LineDaoTest {

    private Long lineId;
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
        LineEntity entity = new LineEntity("수인분당선", "노란색", 700, 7L);
        int beforeSize = lineDao.findAll().size();

        // when
        Long newLineId = lineDao.insert(entity);
        int afterSize = lineDao.findAll().size();

        // then
        assertAll(
            () -> assertThatCode(() -> lineDao.findById(newLineId))
                .doesNotThrowAnyException(),
            () -> assertThat(afterSize).isEqualTo(beforeSize + 1)
        );

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
    @DisplayName("findHeadIdById()를 호출하면 노선의 상행 종점의 id 값을 반환한다")
    void findHeadIdById() {
        // given, when
        Long headId = lineDao.findHeadIdById(lineId);

        // then
        Assertions.assertThat(headId).isEqualTo(1L);
    }

    @Test
    @DisplayName("isExist()를 호출할 때 입력한 name를 갖는 노선이 존재한다면 true를 반환한다")
    void isExist_true() {
        // given, when
        String name = "1호선";
        boolean isExistLine = lineDao.isExist(name);

        // then
        Assertions.assertThat(isExistLine).isTrue();
    }

    @Test
    @DisplayName("isExist()를 호출할 때 입력한 name를 갖는 노선이 존재하지 않는다면 false를 반환한다")
    void isExist_false() {
        // given, when
        String name = "10호선";
        boolean isExistLine = lineDao.isExist(name);

        // then
        Assertions.assertThat(isExistLine).isFalse();
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
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("존재하지 않는 노선입니다.");
    }
}
