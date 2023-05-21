package subway.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

@Sql({"/schema-test.sql", "/data-test.sql"})
@JdbcTest
class StationServiceTest {

    Long lineId;

    private StationDao stationDao;
    private LineDao lineDao;
    private StationService stationService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
        lineDao = new LineDao(jdbcTemplate, dataSource);
        stationService = new StationService(stationDao, lineDao);
        lineId = 1L;
    }

    @Nested
    @DisplayName("saveStation()를 호출할 때")
    class saveStation {

        private int distance = 1;
        private int overDistance = 10;
        private String newStationName = "신림역";

        @Test
        @DisplayName("새 상행 종점을 추가할 수 있다")
        void upEndStation() {
            // given
            String upStationName = "강남역";
            StationRequest stationRequest = new StationRequest(newStationName, upStationName,
                distance);
            int beforeSize = stationDao.findByLineId(lineId).size();

            // when
            stationService.saveStation(lineId, stationRequest);
            List<StationResponse> stations = stationService.findLineStationResponsesById(lineId);
            int afterSize = stations.size();
            boolean isNewUpEndStation = lineDao.isUpEndStation(lineId, newStationName);
            List<String> stationNames = stations.stream().map(StationResponse::getName)
                .collect(Collectors.toList());

            // then
            assertAll(
                () -> Assertions.assertThat(isNewUpEndStation).isTrue(),
                () -> Assertions.assertThat(afterSize).isEqualTo(beforeSize + 1),
                () -> Assertions.assertThat(stationNames)
                    .containsExactly(newStationName, "강남역", "역삼역", "선릉역", "삼성역", "건대입구역", "잠실역")
            );
        }

        @Test
        @DisplayName("새 하행 종점을 추가할 수 있다")
        void downEndStation() {
            // given
            String downStationName = "잠실역";
            StationRequest stationRequest = new StationRequest(downStationName, newStationName,
                distance);
            int beforeSize = stationDao.findByLineId(lineId).size();

            // when
            stationService.saveStation(lineId, stationRequest);
            List<StationResponse> stations = stationService.findLineStationResponsesById(lineId);
            int afterSize = stations.size();
            boolean isNewDownEndStation = stationDao.isDownEndStation(lineId, newStationName);
            List<String> stationNames = stations.stream().map(StationResponse::getName)
                .collect(Collectors.toList());

            // then
            assertAll(
                () -> Assertions.assertThat(isNewDownEndStation).isTrue(),
                () -> Assertions.assertThat(afterSize).isEqualTo(beforeSize + 1),
                () -> Assertions.assertThat(stationNames)
                    .containsExactly("강남역", "역삼역", "선릉역", "삼성역", "건대입구역", "잠실역", newStationName)
            );

        }

        @ParameterizedTest
        @CsvSource(value = {"역삼역:강남역", "선릉역:역삼역", "삼성역:선릉역", "건대입구역:삼성역",
            "잠실역:건대입구역"}, delimiter = ':')
        @DisplayName("기존 역의 상행 위치에 역을 추가할 수 있다")
        void middle_upStation(String criteriaStation, String previousStation) {
            // given
            StationRequest stationRequest = new StationRequest(newStationName, criteriaStation,
                distance);
            String upStationName = stationDao.findByNextStationId(lineId, criteriaStation)
                .getName();
            int beforeUpStationDistance = stationDao.findByLineIdAndName(lineId, upStationName)
                .getDistance();
            int beforeSize = stationDao.findByLineId(lineId).size();

            // when
            stationService.saveStation(lineId, stationRequest);
            List<StationResponse> stations = stationService.findLineStationResponsesById(lineId);
            int afterDistance = stationDao.findByLineIdAndName(lineId, newStationName)
                .getDistance();
            int afterUpStationDistance = stationDao.findByLineIdAndName(lineId, upStationName)
                .getDistance();
            int afterSize = stations.size();
            String stationNames = stations.stream().map(StationResponse::getName)
                .collect(Collectors.joining());

            // then
            assertAll(
                () -> Assertions.assertThat(afterUpStationDistance)
                    .isEqualTo(beforeUpStationDistance - distance),
                () -> Assertions.assertThat(afterDistance)
                    .isEqualTo(distance),
                () -> Assertions.assertThat(afterSize).isEqualTo(beforeSize + 1),
                () -> Assertions.assertThat(stationNames)
                    .contains(previousStation + newStationName + criteriaStation)
            );
        }

        @ParameterizedTest
        @CsvSource(value = {
            "강남역:역삼역", "역삼역:선릉역", "선릉역:삼성역", "삼성역:건대입구역", "건대입구역:잠실역"}, delimiter = ':')
        @DisplayName("기존 역의 하행 위치에 역을 추가할 수 있다")
        void middle_downStation(String criteriaStation, String nextStation) {
            // given
            StationRequest stationRequest = new StationRequest(criteriaStation, newStationName,
                distance);
            int beforeUpStationDistance = stationDao.findByLineIdAndName(lineId, criteriaStation)
                .getDistance();
            int beforeSize = stationDao.findByLineId(lineId).size();

            // when
            stationService.saveStation(lineId, stationRequest);
            List<StationResponse> stations = stationService.findLineStationResponsesById(lineId);
            int afterDistance = stationDao.findByLineIdAndName(lineId, newStationName)
                .getDistance();
            int afterUpStationDistance = stationDao.findByLineIdAndName(lineId, criteriaStation)
                .getDistance();
            int afterSize = stations.size();
            String stationNames = stations.stream().map(StationResponse::getName)
                .collect(Collectors.joining());

            // then
            assertAll(
                () -> Assertions.assertThat(afterUpStationDistance)
                    .isEqualTo(distance),
                () -> Assertions.assertThat(afterDistance)
                    .isEqualTo(beforeUpStationDistance - distance),
                () -> Assertions.assertThat(afterSize).isEqualTo(beforeSize + 1),
                () -> Assertions.assertThat(stationNames)
                    .contains(criteriaStation + newStationName + nextStation)
            );
        }

        @Test
        @DisplayName("새로운 역이 상행 방향으로 추가될 때 새로운 역과 기존 역 간 거리가 기존의 두 역 간 거리보다 크거나 같으면 예외 발생")
        void between_upStation_fail() {
            // given
            String stationName = "선릉역";
            StationRequest stationRequest = new StationRequest(newStationName, stationName,
                overDistance);

            //when, then
            assertThatThrownBy(() -> stationService.saveStation(lineId, stationRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("추가하려는 역이 기존 목적지 역보다 멀리 있습니다");
        }

        @Test
        @DisplayName("새로운 역이 하행 방향으로 추가될 때 새로운 역과 기존 역 간 거리가 기존의 두 역 간 거리보다 크거나 같으면 예외 발생")
        void between_downStation_fail() {
            // given
            String stationName = "역삼역";
            StationRequest stationRequest = new StationRequest(stationName, newStationName,
                overDistance);

            //when, then
            assertThatThrownBy(() -> stationService.saveStation(lineId, stationRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("추가하려는 역이 기존 목적지 역보다 멀리 있습니다");
        }

        @Test
        @DisplayName("상행역과 하행역 모두 노선에 존재할 경우 예외 발생")
        void exist_both_stations_fail() {
            // given
            String stationName1 = "역삼역";
            String stationName2 = "건대입구역";
            StationRequest stationRequest = new StationRequest(stationName1, stationName2,
                distance);

            //when, then
            assertThatThrownBy(() -> stationService.saveStation(lineId, stationRequest))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessage("이미 존재하는 역입니다.");
        }

        @Test
        @DisplayName("상행역과 하행역 모두 노선에 존재하지 않을 경우 예외 발생")
        void not_exist_both_station_fail() {
            // given
            String newStationName2 = "시청역";
            StationRequest stationRequest = new StationRequest(newStationName, newStationName2,
                distance);

            //when, then
            assertThatThrownBy(() -> stationService.saveStation(lineId, stationRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 노선에 기준이 될 역이 없습니다");
        }

    }

    @Test
    @DisplayName("findLineStationResponsesById()를 호출할 때 노선에 역이 존재한다면 상행 종점부터 하행 종점 까지의 모든 역을 순서대로 반환한다.")
    void findLineStationResponsesById_sucess() {
        // given, when
        List<StationResponse> stations = stationService.findLineStationResponsesById(lineId);
        List<String> actual = stations.stream().map(StationResponse::getName)
            .collect(Collectors.toList());

        // then
        Assertions.assertThat(actual).containsExactly("강남역", "역삼역", "선릉역", "삼성역", "건대입구역", "잠실역");

    }

    @Nested
    @DisplayName("deleteStation()를 호출할 때")
    class deleteStation {

        @Test
        @DisplayName("상행 종점을 삭제하면 그 다음 역이 새로운 상행 종점이 된다")
        void deleteUpEndStation_success() {
            // given
            String upEndStationName = "강남역";
            int beforeSize = stationService.findLineStationResponsesById(lineId).size();

            // when
            stationService.deleteStation(lineId, upEndStationName);
            List<StationResponse> after = stationService.findLineStationResponsesById(lineId);
            List<String> afterStationNames = after.stream().map(StationResponse::getName)
                .collect(Collectors.toList());
            int afterSize = after.size();

            // then
            assertAll(
                () -> Assertions.assertThatThrownBy(
                        () -> stationDao.findByLineIdAndName(lineId, upEndStationName))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage(String.format("노선에 %s이 존재하지 않습니다.", upEndStationName)),
                () -> Assertions.assertThat(afterSize).isEqualTo(beforeSize - 1),
                () -> Assertions.assertThat(afterStationNames)
                    .containsExactly("역삼역", "선릉역", "삼성역", "건대입구역", "잠실역")
            );
        }

        @Test
        @DisplayName("하행 종점을 삭제하면 그 전 역이 새로운 하행 종점이 된다")
        void deleteDownEndStation_success() {
            // given
            String downEndStationName = "잠실역";
            int beforeSize = stationService.findLineStationResponsesById(lineId).size();

            // when
            stationService.deleteStation(lineId, downEndStationName);
            List<StationResponse> after = stationService.findLineStationResponsesById(lineId);
            List<String> afterStationNames = after.stream().map(StationResponse::getName)
                .collect(Collectors.toList());
            int afterSize = after.size();

            // then
            assertAll(
                () -> Assertions.assertThatThrownBy(
                        () -> stationDao.findByLineIdAndName(lineId, downEndStationName))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage(String.format("노선에 %s이 존재하지 않습니다.", downEndStationName)),
                () -> Assertions.assertThat(afterSize).isEqualTo(beforeSize - 1),
                () -> Assertions.assertThat(afterStationNames)
                    .containsExactly("강남역", "역삼역", "선릉역", "삼성역", "건대입구역")
            );
        }

        @Test
        @DisplayName("중간 역을 삭제하면 그 전 역의 다음역이 삭제한 역의 다음역이 되고 거리는 그 전역과 삭제한 역의 거리의 합이 된다")
        void deleteMiddleStation_success() {
            // given
            String deleteStationName = "선릉역";
            String upStationName = "역삼역";
            int expectedUpStationDistance = 15;
            int beforeSize = stationService.findLineStationResponsesById(lineId).size();

            // when
            stationService.deleteStation(lineId, deleteStationName);
            List<StationResponse> after = stationService.findLineStationResponsesById(lineId);
            List<String> afterStationNames = after.stream().map(StationResponse::getName)
                .collect(Collectors.toList());
            int afterSize = after.size();
            int actualUpStationDistance = stationDao.findByLineIdAndName(lineId, upStationName)
                .getDistance();

            // then
            assertAll(
                () -> Assertions.assertThatThrownBy(
                        () -> stationDao.findByLineIdAndName(lineId, deleteStationName))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage(String.format("노선에 %s이 존재하지 않습니다.", deleteStationName)),
                () -> Assertions.assertThat(afterSize).isEqualTo(beforeSize - 1),
                () -> Assertions.assertThat(afterStationNames)
                    .containsExactly("강남역", "역삼역", "삼성역", "건대입구역", "잠실역"),
                () -> Assertions.assertThat(actualUpStationDistance)
                    .isEqualTo(expectedUpStationDistance)
            );
        }

        @Test
        @DisplayName("노선에 존재하지 않은 역을 삭제하면 예외를 발생시킨다")
        void fail() {
            // given
            String notExistStationName = "없는역";

            // when, then
            Assertions.assertThatThrownBy(
                    () -> stationService.deleteStation(lineId, notExistStationName))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(String.format("노선에 %s이 존재하지 않습니다.", notExistStationName));
        }
    }
}
