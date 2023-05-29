package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.fare.FareCalculator;
import subway.domain.section.SectionRepository;
import subway.domain.station.StationRepository;
import subway.dto.PathResponse;

import java.util.Set;

import static fixtures.LineFixtures.LINE2;
import static fixtures.PathFixtures.*;
import static fixtures.SectionFixtures.DISTANCE_잠실역_TO_건대역;
import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @InjectMocks
    PathService pathService;

    @Mock
    StationRepository stationRepository;
    @Mock
    SectionRepository sectionRepository;
    @Mock
    FareCalculator fareCalculator;

    @Test
    @DisplayName("최단거리 경로를 찾는다.")
    void findShortestPathTest() {
        // given
        when(sectionRepository.findAllSections()).thenReturn(ALL_SECTIONS);
        when(stationRepository.findStationById(STATION_잠실역_ID)).thenReturn(STATION_잠실역);
        when(stationRepository.findStationById(STATION_건대역_ID)).thenReturn(STATION_건대역);
        when(fareCalculator.calculate(DISTANCE_잠실역_TO_건대역, Set.of(LINE2))).thenReturn(FARE_잠실역_TO_건대역);

        // when
        PathResponse response = pathService.findShortestPath(STATION_잠실역_ID, STATION_건대역_ID);

        // then
        assertThat(response).isEqualTo(RESPONSE_PATH_잠실역_TO_건대역);
    }
}