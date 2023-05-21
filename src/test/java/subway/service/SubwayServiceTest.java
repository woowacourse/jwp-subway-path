package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.controller.dto.StationResponse;
import subway.controller.dto.SubwayShortestPathResponse;
import subway.domain.Path;
import subway.domain.line.Line;
import subway.domain.passenger.Passenger;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.domain.subway.Subway;
import subway.domain.subway.billing_policy.Fare;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
class SubwayServiceTest {

    @InjectMocks
    private SubwayService subwayService;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private Subway subway;

    @Nested
    @DisplayName("두 역 사이의 최단 거리 경로를 조회할 시 ")
    class FindShortestPath {

        private Station upward;
        private Station middle;
        private Station downward;
        private Line lineTwo;
        private Line lineFour;

        @BeforeEach
        void setUp() {
            upward = new Station(1L, "잠실역");
            middle = new Station(2L, "사당역");
            downward = new Station(3L, "서울역");
            final List<Section> sectionForLineTwo = List.of(new Section(upward, middle, 3));
            final List<Section> sectionsForLineFour = List.of(new Section(middle, downward, 4));
            lineTwo = new Line(1L, "2호선", "초록색", 300, new LinkedList<>(sectionForLineTwo));
            lineFour = new Line(2L, "4호선", "하늘색", 400, new LinkedList<>(sectionsForLineFour));
            given(lineRepository.findAll()).willReturn(List.of(lineTwo, lineFour));
        }

        @Test
        @DisplayName("유효한 정보라면 최단 거리 경로, 요금을 반환한다.")
        void findShortestPath() {

            given(stationRepository.findById(1L)).willReturn(upward);
            given(stationRepository.findById(3L)).willReturn(downward);
            willDoNothing().given(subway).updateRouteMap(anyList());
            given(subway.findShortestPath(any(Station.class), any(Station.class))).willReturn(
                    new Path(List.of(upward, middle, downward), List.of(lineTwo, lineFour), 7));
            given(subway.calculateFare(any(Path.class), any(Passenger.class))).willReturn(new Fare(1250));

            final SubwayShortestPathResponse shortestPath = subwayService.findShortestPath(1L, 3L, 20);

            assertAll(
                    () -> assertThat(shortestPath.getStations()).extracting(StationResponse::getName)
                            .containsExactly("잠실역", "사당역", "서울역"),
                    () -> assertThat(shortestPath.getDistance()).isEqualTo(7),
                    () -> assertThat(shortestPath.getFare()).isEqualTo(1250)
            );
        }
    }
}
