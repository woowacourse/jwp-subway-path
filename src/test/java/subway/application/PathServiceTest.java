package subway.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.application.fare.FareCalculator;
import subway.application.path.PathFinder;
import subway.application.path.PathService;
import subway.domain.Distance;
import subway.domain.Fare;
import subway.domain.ShortestPath;
import subway.domain.Station;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @InjectMocks
    private PathService pathService;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private SectionRepository sectionRepository;
    @Mock
    private PathFinder pathFinder;
    @Mock
    private FareCalculator fareCalculator;

    @Test
    void 경로_조회를_테스트한다() {
        // given
        final Station 석촌 = new Station(5L, "석촌");
        final Station 잠실 = new Station(1L, "잠실");
        final Station 잠실새내 = new Station(2L, "잠실새내");
        final ShortestPath shortestPath = new ShortestPath(List.of(석촌, 잠실, 잠실새내), Distance.from(25));

        given(sectionRepository.findAll()).willReturn(Collections.emptyList());
        given(stationRepository.findById(석촌.getId())).willReturn(석촌);
        given(stationRepository.findById(잠실새내.getId())).willReturn(잠실새내);
        given(fareCalculator.calculateFare(any(Distance.class))).willReturn(Fare.from(1550));
        given(pathFinder.findShortestPath(anyList(), any(Station.class), any(Station.class))).willReturn(shortestPath);

        // when
        pathService.findShortestPath(석촌.getId(), 잠실새내.getId());

        // then
        assertAll(
                () -> then(sectionRepository).should(only()).findAll(),
                () -> then(stationRepository).should(times(2)).findById(anyLong()),
                () -> then(pathFinder).should(only()).findShortestPath(anyList(), any(Station.class), any(Station.class))
        );
    }
}
