package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import subway.entity.StationEntity;

@Sql({"/schema-test.sql", "/data-test.sql"})
@JdbcTest
class StationDaoTest {

    private Long lineId;
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
    @DisplayName("insert()를 호출하면 입력받은 역을 추가한다.")
    void insert() {
        // given
        String newStationName = "성수역";
        StationEntity stationEntity = new StationEntity(newStationName, 4L, 4, lineId);
        int beforeSize = stationDao.findByLineId(lineId).size();

        // when
        stationDao.insert(stationEntity);
        int afterSize = stationDao.findByLineId(lineId).size();

        // then
        assertAll(
            () -> assertThatCode(() -> stationDao.findByLineIdAndName(lineId, newStationName))
                .doesNotThrowAnyException(),
            () -> assertThat(afterSize).isEqualTo(beforeSize + 1)
        );
    }

    @Test
    @DisplayName("findAll()를 호출하면 모든 역을 반환한다")
    void findAll() {
        // given
        int expected = 14;

        // when
        List<StationEntity> actual = stationDao.findAll();

        // then
        Assertions.assertThat(actual).hasSize(expected);
    }

    @Test
    @DisplayName("findByLineId()를 호출하면 노선의 모든 역을 반환한다")
    void findByLineId() {
        // given
        int expected = 6;

        // when
        List<StationEntity> actual = stationDao.findByLineId(lineId);

        // then
        Assertions.assertThat(actual).hasSize(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"역삼역:1", "선릉역:2", "삼성역:4", "건대입구역:6", "잠실역:5"}, delimiter = ':')
    @DisplayName("findByNextStationId()를 호출할 때 이전 역이 존재한다면 입력받은 역을 다음 역으로 갖는 역을 반환한다")
    void findByNextStationId_success(String existPreviousStation, Long expected) {
        // given, when
        StationEntity previousStation = stationDao.findByNextStationId(lineId,
            existPreviousStation);
        Long actual = previousStation.getId();

        // then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"강남역", "없는역"})
    @DisplayName("findByNextStationId()를 호출할 때 이전 역이 존재하지 않는다면 예외를 반환한다")
    void findByNextStationId_fail(String notExistPreviousStation) {
        // given, when, then
        Assertions.assertThatThrownBy(
                () -> stationDao.findByNextStationId(lineId, notExistPreviousStation))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("노선에 존재하지 않는 역이거나 이전 역이 존재하지 않는 역입니다.");
    }

    @Test
    @DisplayName("findByLineIdAndName()를 호출하면 입력받은 노선 번호와 이름을 가진 역의 정보를 찾아 반환한다")
    void findByLineIdAndName() {
        // given
        String stationName = "역삼역";
        StationEntity expected=new StationEntity(2L, stationName,4L,5,lineId);

        // when
        StationEntity actual = stationDao.findByLineIdAndName(lineId, stationName);

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

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

    @Test
    @DisplayName("isDownEndStation()를 호출할 때 입력받은 이름의 역이 노선의 하행종점이라면 true를 반환한다")
    void isDownEndStation_true() {
        // given
        String downEndStationName = "잠실역";

        // when
        boolean actual = stationDao.isDownEndStation(lineId, downEndStationName);

        // then
        Assertions.assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"역삼역", "강남역", "선릉역", "건대입구역", "삼성역", "없는역"})
    @DisplayName("isDownEndStation()를 호출할 때 입력받은 이름의 역이 노선의 하행종점이 아니라면 false를 반환한다")
    void isDownEndStation_false(String remainStationName) {
        // given, when
        boolean actual = stationDao.isDownEndStation(lineId, remainStationName);

        // then
        Assertions.assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("isExistInLine()를 호출할 때 입력한 name를 갖는 역이 노선에 존재한다면 true를 반환한다")
    void isExistInLine_true() {
        // given, when
        String name = "선릉역";
        boolean isExistLine = stationDao.isExistInLine(lineId, name);

        // then
        Assertions.assertThat(isExistLine).isTrue();
    }

    @Test
    @DisplayName("isExistInLine()를 호출할 때 입력한 name를 갖는 역이 노선에 존재하지 않는다면 false를 반환한다")
    void isExistInLine_false() {
        // given, when
        String name = "없는역";
        boolean isExistLine = stationDao.isExistInLine(lineId, name);

        // then
        Assertions.assertThat(isExistLine).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"선릉역", "왕십리역", "강남역", "잠실역"})
    @DisplayName("isNotExist()를 호출할 때 입력한 name를 갖는 역이 모든 노선을 통틀어 존재한다면 false를 반환한다")
    void isNotExist_false(String name) {
        // given, when
        boolean isNotExist = stationDao.isNotExist(name);

        // then
        Assertions.assertThat(isNotExist).isFalse();
    }

    @Test
    @DisplayName("isExist()를 호출할 때 입력한 name를 갖는 역이 모든 노선을 통틀어 존재하지 않는다면 true를 반환한다")
    void isNotExist_true() {
        // given, when
        String name = "없는역";
        boolean isNotExist = stationDao.isNotExist(name);

        // then
        Assertions.assertThat(isNotExist).isTrue();
    }

    @Test
    @DisplayName("updateNextStationById()를 호출하면 next_station 값을 변경한다")
    void updateNextStationById() {
        // given
        String stationName = "선릉역";
        StationEntity stationEntity = stationDao.findByLineIdAndName(lineId, stationName);
        Long stationId = stationEntity.getId();
        Long before = stationEntity.getNext();
        Long newNextId = 0L;

        // when
        stationDao.updateNextStationById(stationId, newNextId);
        Long after = stationDao.findByLineIdAndName(lineId, stationName).getNext();

        // then
        assertAll(
            () -> Assertions.assertThat(before).isNotEqualTo(after),
            () -> Assertions.assertThat(after).isEqualTo(newNextId)
        );
    }

    @Test
    @DisplayName("updateDistanceById()를 호출할 때 입력받은 새 거리가 양의 정수라면 Distance 값을 변경한다")
    void updateDistanceById_success() {
        // given
        String stationName = "선릉역";
        StationEntity stationEntity = stationDao.findByLineIdAndName(lineId, stationName);
        Long stationId = stationEntity.getId();
        int before = stationEntity.getDistance();
        int newDistance = 3;

        // when
        stationDao.updateDistanceById(stationId, newDistance);
        int after = stationDao.findByLineIdAndName(lineId, stationName).getDistance();

        // then
        assertAll(
            () -> Assertions.assertThat(before).isNotEqualTo(after),
            () -> Assertions.assertThat(after).isEqualTo(newDistance)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    @DisplayName("updateDistanceById()를 호출할 때 입력받은 새 거리가 양의 정수가 아니라면 예외를 발생시킨다")
    void updateDistanceById_fail(int notValidDistance) {
        // given
        String stationName = "선릉역";
        StationEntity stationEntity = stationDao.findByLineIdAndName(lineId, stationName);
        Long stationId = stationEntity.getId();

        // when, then
        assertThatThrownBy(() -> stationDao.updateDistanceById(stationId, notValidDistance))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("거리는 양의 정수여야 합니다");
    }

    @Test
    @DisplayName("deleteById()를 호출하면 입력받은 역을 제거한다.")
    void deleteById() {
        // given
        String stationName = "선릉역";
        StationEntity stationEntity = stationDao.findByLineIdAndName(lineId, stationName);
        Long stationId = stationEntity.getId();

        // when
        stationDao.deleteById(stationId);

        // then
        Assertions.assertThatThrownBy(() -> stationDao.findByLineIdAndName(lineId, stationName))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(String.format("노선에 %s이 존재하지 않습니다.", stationName));
    }

    @Test
    @DisplayName("deleteByLineId()를 호출하면 입력받은 노선에 포함된 역들을 모두 제거한다.")
    void deleteByLineId() {
        // given
        int expected = 0;

        // when
        stationDao.deleteByLineId(lineId);
        List<StationEntity> actual = stationDao.findByLineId(lineId);

        // then
        Assertions.assertThat(actual).hasSize(expected);
    }
}
