package subway.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Path;
import subway.domain.Station;
import subway.domain.StationEdge;
import subway.domain.StationEdges;
import subway.dto.service.PathResult;
import subway.exception.ArrivalSameWithDepartureException;
import subway.exception.PathNotExistsException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@SpringBootTest
@Transactional
class PathServiceTest {

    public static Station 강남;
    public static Station 역삼;
    public static Station 잠실;
    @Autowired
    private PathService pathService;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @Nested
    class ShortestPath {

        @BeforeEach
        void setUp() {
            강남 = new Station(stationRepository.create(new Station("강남")), "강남");
            역삼 = new Station(stationRepository.create(new Station("역삼")), "역삼");
            잠실 = new Station(stationRepository.create(new Station("잠실")), "잠실");
            Line 이호선 = new Line("2호선", "초록색", StationEdges.from(
                    List.of(new StationEdge(강남.getId(), 0), new StationEdge(역삼.getId(), 10), new StationEdge(잠실.getId(), 10))));
            lineRepository.create(이호선);
        }

        @Test
        @DisplayName("최단 경로를 계산할 수 있다.")
        void calculateShortestPath() {
            //given
            PathResult shortestPath = pathService.getShortestPath(강남, 역삼);
            //when
            //then
            Distance distance = shortestPath.getPath().calculateTotalDistance();
            System.out.println(distance.toString());
        }

        @Test
        @DisplayName("경로가 존재하지 않는 두 역의 최단 경로를 구한다.")
        void invalidPath() {
            //given
            Station 해운대 = new Station(stationRepository.create(new Station("해운대")), "해운대");
            //when
            //then
            assertThatThrownBy(() -> pathService.getShortestPath(강남, 해운대))
                    .isInstanceOf(PathNotExistsException.class);
        }

        @Test
        @DisplayName("같은 역을 출발지와 도착지로 두면 예외를 던진다.")
        void sameStation() {
            //given
            Station station = 잠실;
            //when
            //then
            assertThatThrownBy(() -> pathService.getShortestPath(station, 잠실))
                    .isInstanceOf(ArrivalSameWithDepartureException.class);
        }

    }
}