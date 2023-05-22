package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static subway.TestFixture.STATION_A;
import static subway.TestFixture.STATION_B;
import static subway.TestFixture.STATION_C;
import static subway.TestFixture.STATION_D;
import static subway.TestFixture.STATION_E;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.persistence.LineRepository;
import subway.persistence.StationDao;

@ExtendWith(MockitoExtension.class)
class SubwayServiceTest {

    public static final Line LINE_A = new Line(4L, "99호선", "gray");
    public static final Line LINE_B = new Line(5L, "100호선", "white");

    static {
        LINE_A.add(new Section(STATION_A, STATION_C, 1));
        LINE_A.add(new Section(STATION_C, STATION_D, 1));
        LINE_A.add(new Section(STATION_D, STATION_E, 6));
        LINE_B.add(new Section(STATION_A, STATION_B, 1));
        LINE_B.add(new Section(STATION_B, STATION_C, 1));
        LINE_B.add(new Section(STATION_C, STATION_E, 6));
    }

    public static final List<Station> SHORTEST_PATH_STATIONS_IN_LINE_A_AND_B_STATION_A_TO_E = List.of(
            STATION_A, STATION_C, STATION_E
    );

    @InjectMocks
    private SubwayService subwayService;

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationDao stationDao;

    @DisplayName("최단 경로를 구한다")
    @Test
    void findShortestPath() {
        doReturn(List.of(
                LINE_A,
                LINE_B
        )).when(lineRepository).findAll();
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
