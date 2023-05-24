package subway.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;
import static subway.domain.LineFixture.findAllLineFixtures;
import static subway.domain.LineFixture.findAllStationsInLineFixtures;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.exception.PathNotExistsException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;
import subway.service.GraphPathFinderService;

@ExtendWith(MockitoExtension.class)
class PathFinderServiceTest {
    @InjectMocks
    private GraphPathFinderService pathFinder;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    @Nested
    @DisplayName("경로가 존재하는 경우")
    class Success {
        private final List<Line> lines = findAllLineFixtures();
        private final List<Station> stations = findAllStationsInLineFixtures();

        @BeforeEach
        void setUp() {
            given(lineRepository.findAll()).willReturn(lines);
            given(stationRepository.findAll()).willReturn(stations);
        }

        @Test
        @DisplayName("경로를 탐색한다.")
        void findPath() {
            //given
            Station departure = stations.get(0);
            Station arrival = stations.get(3);

            //when
            Path path = pathFinder.findPath(departure, arrival);

            //then
            List<PathSegment> pathSegments = path.getPathSegments();
            PathSegment firstLine = pathSegments.get(0);
            PathSegment nextLine = pathSegments.get(1);

            /**
             * firstLine: 잠실-건대-동대문역사문화공원
             * nextLine: 동대문역사문화공원-혜화
             */
            assertSoftly(softly -> {
                softly.assertThat(firstLine.getLineId()).isEqualTo(lines.get(0).getId());
                softly.assertThat(nextLine.getLineId()).isEqualTo(lines.get(1).getId());

                softly.assertThat(firstLine.getStationEdges()).hasSize(2);
                softly.assertThat(nextLine.getStationEdges()).hasSize(1);
            });
        }
    }

    @Nested
    @DisplayName("경로가 존재하지 않는 경우")
    class Fail {
        private final List<Line> lines = findAllLineFixtures();
        private final List<Station> stations = findAllStationsInLineFixtures();

        private final Station unConnectedStation = new Station(100L, "역이름");

        @BeforeEach
        void setUp() {
            stations.add(new Station(100L, "역이름"));
            stations.add(new Station(101L, "다른역이름"));
            lines.add(
                    new Line(4L, "노선이름", "색", StationEdges.from(List.of(
                            new StationEdge(100L, 0), new StationEdge(101L, 5)
                    )))
            );

            given(lineRepository.findAll()).willReturn(lines);
            given(stationRepository.findAll()).willReturn(stations);
        }

        @Test
        @DisplayName("경로를 탐색한다.")
        void findPath() {
            //given
            Station departure = stations.get(0);

            //when
            //then
            assertThatThrownBy(() -> pathFinder.findPath(departure, unConnectedStation))
                    .isInstanceOf(PathNotExistsException.class);
        }
    }
}