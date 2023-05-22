package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.application.fare.DistanceOver10Under50;
import subway.application.fare.DistanceRateFareCalculator;
import subway.application.fare.FareCalculator;
import subway.application.path.PathFinder;
import subway.domain.Distance;
import subway.domain.Fare;
import subway.domain.ShortestPath;
import subway.domain.Station;
import subway.domain.section.MultiLineSections;
import subway.dto.ShortestPathResponse;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    private PathService pathService;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private SectionRepository sectionRepository;
    @Mock
    private PathFinder pathFinder;
    private List<FareCalculator> fareCalculators;
    private DistanceOver10Under50 distanceOver10Under50;

    @BeforeEach
    void init() {
        distanceOver10Under50 = mock(DistanceOver10Under50.class);
        final DistanceRateFareCalculator distanceRateFareCalculator = new DistanceRateFareCalculator(List.of(distanceOver10Under50));
        pathService = new PathService(stationRepository, sectionRepository, pathFinder, List.of(distanceRateFareCalculator));
    }

    @Test
    void 경로_조회를_테스트한다() {
        // given
        final Station 석촌 = new Station(5L, "석촌");
        final Station 잠실 = new Station(1L, "잠실");
        final Station 잠실새내 = new Station(2L, "잠실새내");
        final ShortestPath shortestPath = new ShortestPath(List.of(석촌, 잠실, 잠실새내), Distance.from(25));

        given(sectionRepository.findAll()).willReturn(mock(MultiLineSections.class));
        given(stationRepository.findById(anyLong())).willReturn(mock(Station.class));
        given(distanceOver10Under50.calculateFare(any(Distance.class))).willReturn(Fare.from(300));
        given(pathFinder.findShortestPath(any(MultiLineSections.class), any(Station.class), any(Station.class))).willReturn(shortestPath);

        // when
        final ShortestPathResponse result = pathService.findShortestPath(석촌.getId(), 잠실새내.getId());

        // then
        assertAll(
                () -> then(sectionRepository).should(only()).findAll(),
                () -> then(stationRepository).should(times(2)).findById(anyLong()),
                () -> then(pathFinder).should(only()).findShortestPath(any(MultiLineSections.class), any(Station.class), any(Station.class)),
                () -> assertThat(result.getFare()).isEqualTo(1550),
                () -> assertThat(result.getRoutes()).hasSize(3)
        );
    }
}
