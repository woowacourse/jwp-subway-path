package subway.service;

import helper.IntegrationTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.dao.SectionDao;
import subway.dao.SectionEntity;
import subway.service.dto.StationDeleteRequest;
import subway.service.dto.StationRegisterRequest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StationServiceTest extends IntegrationTestHelper {

    @Autowired
    private StationService stationService;

    @Autowired
    private SectionDao sectionDao;

    @Test
    @DisplayName("registerStation() : 역을 저장할 수 있다.")
    void test_registerStation() throws Exception {
        //given
        final String currentStationName = "G";
        final String nextStationName = "K";
        final String lineName = "2호선";
        final int distance = 1;

        final StationRegisterRequest stationRegisterRequest =
                new StationRegisterRequest(lineName, currentStationName, nextStationName, distance);

        //when
        stationService.registerStation(stationRegisterRequest);

        final SectionEntity sectionEntity =
                sectionDao.findSectionsByLineId(2L)
                          .stream()
                          .filter(it -> it.getId().equals(6L))
                          .findAny()
                          .orElseThrow();

        //then
        assertAll(
                () -> assertEquals("K", sectionEntity.getCurrentStationName()),
                () -> assertEquals("H", sectionEntity.getNextStationName()),
                () -> assertEquals(4, sectionEntity.getDistance())
        );
    }

    @Test
    @DisplayName("deleteStation() : 해당 역을 삭제할 수 있다.")
    void test_deleteStation() throws Exception {
        //given
        final String lineName = "1호선";
        final String stationName = "B";

        final StationDeleteRequest stationDeleteRequest = new StationDeleteRequest(lineName, stationName);

        //when
        stationService.deleteStation(stationDeleteRequest);

        //then

        final SectionEntity sectionEntity =
                sectionDao.findSectionsByLineId(1L)
                          .stream()
                          .filter(it -> it.getId().equals(1L))
                          .findAny()
                          .orElseThrow();

        assertAll(
                () -> assertEquals("C", sectionEntity.getNextStationName()),
                () -> assertEquals(3, sectionEntity.getDistance())
        );
    }

    @Test
    @DisplayName("deleteStation() : 라인에 역이 하나만 존재하게 되면 라인이 같이 삭제될 수 있다.")
    void test_deleteStation_deleteLine() throws Exception {
        //given
        final String lineName = "1호선";
        final String stationName1 = "B";
        final String stationName2 = "C";

        final String stationName3 = "A";

        final StationDeleteRequest stationDeleteRequest = new StationDeleteRequest(lineName, stationName1);
        stationService.deleteStation(new StationDeleteRequest(lineName, stationName1));
        stationService.deleteStation(new StationDeleteRequest(lineName, stationName2));

        //when
        stationService.deleteStation(new StationDeleteRequest(lineName, stationName3));

        //then
        final int size = sectionDao.findSectionsByLineId(1L).size();

        assertEquals(0, size);
    }
}
