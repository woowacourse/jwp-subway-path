package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.dao.StubStationDao;
import subway.domain.station.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

class StationServiceTest {

    private StubStationDao stubStationDao;
    private StationService stationService;

    @BeforeEach
    void setUp() {
        stubStationDao = new StubStationDao();
        stationService = new StationService(stubStationDao);
    }

    @DisplayName("역을 저장한다.")
    @Test
    void saveStation() {
        final Long stationId = stationService.saveStation(new StationRequest("강남역")).getId();
        final Station result = stubStationDao.findById(stationId);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(stationId),
                () -> assertThat(result.getName()).isEqualTo("강남역")
        );
    }

    @DisplayName("아이디로 역을 조회한다.")
    @Test
    void findStationResponseById() {
        final Long stationId = stubStationDao.insert(new Station("잠실역")).getId();
        final StationResponse result = stationService.findStationResponseById(stationId);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(stationId),
                () -> assertThat(result.getName()).isEqualTo("잠실역")
        );
    }

    @DisplayName("모든 역을 조회한다.")
    @Test
    void findAllStationResponses() {
        final Long stationId1 = stubStationDao.insert(new Station("강남역")).getId();
        final Long stationId2 = stubStationDao.insert(new Station("잠실역")).getId();
        final List<StationResponse> result = stationService.findAllStationResponses();

        assertAll(
                () -> assertThat(result.get(0).getId()).isEqualTo(stationId1),
                () -> assertThat(result.get(0).getName()).isEqualTo("강남역"),
                () -> assertThat(result.get(1).getId()).isEqualTo(stationId2),
                () -> assertThat(result.get(1).getName()).isEqualTo("잠실역")
        );
    }

    @DisplayName("역을 업데이트한다.")
    @Test
    void updateStation() {
        final Long stationId = stubStationDao.insert(new Station("강남역")).getId();
        stationService.updateStation(stationId, new StationRequest("사당역"));
        final Station result = stubStationDao.findById(stationId);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(stationId),
                () -> assertThat(result.getName()).isEqualTo("사당역")
        );
    }

    @DisplayName("역을 삭제한다.")
    @Test
    void deleteStationById() {
        final Long stationId = stubStationDao.insert(new Station("강남역")).getId();
        stationService.deleteStationById(stationId);
        final Station result = stubStationDao.findById(stationId);
        assertThat(result).isNull();
    }
}
