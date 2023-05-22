package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static subway.TestFixture.LINE_A;
import static subway.TestFixture.LINE_B;
import static subway.TestFixture.SHORTEST_PATH_STATIONS_IN_LINE_A_AND_B_STATION_A_TO_E;
import static subway.TestFixture.STATION_A;
import static subway.TestFixture.STATION_E;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.domain.Station;
import subway.persistence.StationDao;

@ExtendWith(MockitoExtension.class)
class SubwayServiceTest {

    @InjectMocks
    private SubwayService subwayService;

    @Mock
    private LineService lineService;
    @Mock
    private StationDao stationDao;

    @DisplayName("최단 경로를 구한다")
    @Test
    void findShortestPath() {
        doReturn(List.of(
                LINE_A,
                LINE_B
        )).when(lineService).findAll();
        doReturn(STATION_A).when(stationDao).findById(STATION_A.getId());
        doReturn(STATION_E).when(stationDao).findById(STATION_E.getId());
        var expectedIds = SHORTEST_PATH_STATIONS_IN_LINE_A_AND_B_STATION_A_TO_E.stream()
                .map(Station::getId)
                .collect(Collectors.toList());

        var path = subwayService.getShortestPath(STATION_A.getId(), STATION_E.getId());

        assertThat(path.getStations())
                .extracting("id")
                .containsExactlyElementsOf(expectedIds);
        assertThat(path.getDistance()).isEqualTo(7);
        assertThat(path.getFare()).isEqualTo(1250);
    }
}
