package subway.application;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.dto.StationResponse;

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
                    .isInstanceOf(IllegalArgumentException.class)
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
                    .isInstanceOf(IllegalArgumentException.class)
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
                    .isInstanceOf(IllegalArgumentException.class)
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
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("노선에 %s이 존재하지 않습니다.", notExistStationName));
        }
    }
}