package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.PathResponse;
import subway.dto.StationResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;

    private PathService pathService;

    @BeforeEach
    void setUp() {
        pathService = new PathService(stationRepository, lineRepository);
    }

    @Test
    @DisplayName("같은 노선에서 두 역 사이의 최단 경로를 구할 수 있다.")
    void findShortestPathInSameLine() {
        /*
         *   [용산역] - [죽전역] - [감삼역]
         *
         *  용산역 - 죽전역 : 10km
         *  죽전역 - 감삼역 : 6km
         */
        // given
        final Station station1 = new Station(1L, "용산역");
        final Station station2 = new Station(2L, "죽전역");
        final Station station3 = new Station(3L, "감삼역");
        final Section section1 = new Section(station1, station2, 10);
        final Section section2 = new Section(station2, station3, 6);
        final Line line = new Line(1L, "2호선", "bg-green-500", new Sections(List.of(section1, section2)));
        given(stationRepository.findById(1L)).willReturn(station1);
        given(stationRepository.findById(3L)).willReturn(station3);
        given(lineRepository.findAll()).willReturn(List.of(line));

        // when
        final PathResponse pathResponse = pathService.findShortestPath(station1.getId(), station3.getId());

        // then
        assertThat(pathResponse.getStations())
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        StationResponse.of(station1),
                        StationResponse.of(station2),
                        StationResponse.of(station3)
                ));
        assertThat(pathResponse.getDistance()).isEqualTo(16);
        assertThat(pathResponse.getFare()).isEqualTo(1450);
    }

    @Test
    @DisplayName("서로 다른 2개의 노선에서 두 역 사이의 최단 경로를 구할 수 있다.")
    void findShortestPathInTwoLines() {
        /*
         *   [중앙로역] - [반월당역]
         *                 |
         *             [청라언덕역]
         *
         *  중앙로역 - 반월당역 : 7km
         *  반월당역 - 청라언덕역 : 5km
         */
        // given
        final Station station1 = new Station(1L, "중앙로역");
        final Station station2 = new Station(2L, "반월당역");
        final Station station3 = new Station(3L, "청라언덕역");
        final Section section1 = new Section(station1, station2, 7);
        final Section section2 = new Section(station2, station3, 5);
        final Line line1 = new Line(1L, "1호선", "bg-red-500", new Sections(List.of(section1)));
        final Line line2 = new Line(2L, "2호선", "bg-green-600", new Sections(List.of(section2)));
        given(stationRepository.findById(1L)).willReturn(station1);
        given(stationRepository.findById(3L)).willReturn(station3);
        given(lineRepository.findAll()).willReturn(List.of(line1, line2));

        // when
        final PathResponse pathResponse = pathService.findShortestPath(station1.getId(), station3.getId());

        // then
        assertThat(pathResponse.getStations())
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        StationResponse.of(station1),
                        StationResponse.of(station2),
                        StationResponse.of(station3))
                );
        assertThat(pathResponse.getDistance()).isEqualTo(12);
        assertThat(pathResponse.getFare()).isEqualTo(1350);
    }

    @Test
    @DisplayName("서로 다른 3개의 노선이 삼각형 형태로 연결된 상태에서 두 역 사이의 최단 경로를 구할 수 있다.")
    void findShortestPathInTriangle() {
        /*
         *   [동대구역] - [반월당역]
         *       |        /
         *        [신천역]
         *
         *  동대구역 - 신천역 : 1km
         *  신천역 - 반월당역 : 1km
         *  동대구역 - 반월당역 : 100km
         */
        // given
        final Station station1 = new Station(1L, "동대구역");
        final Station station2 = new Station(2L, "신천역");
        final Station station3 = new Station(3L, "반월당역");
        final Section section1 = new Section(station1, station2, 1);
        final Section section2 = new Section(station2, station3, 1);
        final Section section3 = new Section(station1, station3, 100);
        final Line line1 = new Line(1L, "1호선", "bg-red-500", new Sections(List.of(section1)));
        final Line line2 = new Line(2L, "2호선", "bg-green-600", new Sections(List.of(section2)));
        final Line line3 = new Line(3L, "3호선", "bg-yellow-600", new Sections(List.of(section3)));
        given(stationRepository.findById(1L)).willReturn(station1);
        given(stationRepository.findById(3L)).willReturn(station3);
        given(lineRepository.findAll()).willReturn(List.of(line1, line2, line3));

        // when
        final PathResponse pathResponse = pathService.findShortestPath(station1.getId(), station3.getId());

        // then
        assertThat(pathResponse.getStations())
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        StationResponse.of(station1),
                        StationResponse.of(station2),
                        StationResponse.of(station3))
                );
        assertThat(pathResponse.getDistance()).isEqualTo(2);
        assertThat(pathResponse.getFare()).isEqualTo(1250);
    }

    @Test
    @DisplayName("2번 환승하는 경우의 최단 경로를 구할 수 있다.")
    void findShortestPathWithTwoTransfers() {
        /*
         *   [A역] - [B역] - [C역]
         *            |      |
         *          [D역] - [E역]
         *
         *  A역 - B역 (1호선) : 1km
         *  B역 - C역 (1호선) : 100km
         *  C역 - E역 (1호선) : 1km
         *  B역 - D역 (2호선) : 1km
         *  D역 - E역 (2호선) : 1km
         */
        // given
        final Station station1 = new Station(1L, "A역");
        final Station station2 = new Station(2L, "B역");
        final Station station3 = new Station(3L, "C역");
        final Station station4 = new Station(4L, "D역");
        final Station station5 = new Station(5L, "E역");
        final Section section1 = new Section(station1, station2, 1);
        final Section section2 = new Section(station2, station3, 100);
        final Section section3 = new Section(station3, station5, 1);
        final Section section4 = new Section(station2, station4, 1);
        final Section section5 = new Section(station4, station5, 1);
        final Line line1 = new Line(1L, "1호선", "bg-red-500",
                new Sections(List.of(section1, section2, section3)));
        final Line line2 = new Line(2L, "2호선", "bg-green-600",
                new Sections(List.of(section4, section5)));
        given(stationRepository.findById(1L)).willReturn(station1);
        given(stationRepository.findById(3L)).willReturn(station3);
        given(lineRepository.findAll()).willReturn(List.of(line1, line2));

        // when
        final PathResponse pathResponse = pathService.findShortestPath(station1.getId(), station3.getId());

        // then
        assertThat(pathResponse.getStations())
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        StationResponse.of(station1),
                        StationResponse.of(station2),
                        StationResponse.of(station4),
                        StationResponse.of(station5),
                        StationResponse.of(station3))
                );
        assertThat(pathResponse.getDistance()).isEqualTo(4);
        assertThat(pathResponse.getFare()).isEqualTo(1250);
    }
}
