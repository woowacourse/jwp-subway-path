package subway.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.fare.FareCalculator;
import subway.domain.path.PathInfo;
import subway.domain.path.strategy.PathFindStrategy;
import subway.domain.subway.Line;
import subway.domain.subway.Section;
import subway.domain.subway.Station;
import subway.dto.PathFindingRequest;
import subway.dto.StationResponse;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    private final Set<Line> lines = Set.of(
        new Line(1L, "1호선", "파랑", 0),
        new Line(2L, "2호선", "초록", 200)
    );
    private final List<Station> stations = List.of(
        new Station(1L, "잠실역"),
        new Station(3L, "잠실나루역"),
        new Station(4L, "신림역")
    );
    private final List<Section> sections = List.of(
        new Section(1L, 1L, 2L, 1L, 5),
        new Section(1L, 2L, 3L, 1L, 5),
        new Section(1L, 3L, 4L, 1L, 5),
        new Section(1L, 1L, 3L, 2L, 2),
        new Section(1L, 4L, 5L, 1L, 5),
        new Section(1L, 5L, 6L, 1L, 5),
        new Section(1L, 6L, 7L, 1L, 50)
    );
    @InjectMocks
    private PathService pathService;
    @Mock
    private LineDao lineDao;
    @Mock
    private StationDao stationDao;
    @Mock
    private SectionDao sectionDao;
    @Mock
    private FareCalculator fareCalculator;
    @Mock
    private PathFindStrategy pathFindStrategy;

    @BeforeEach
    void setUp() {
        when(sectionDao.findAll()).thenReturn(sections);
        when(lineDao.findAll()).thenReturn(new ArrayList<>(lines));
        when(stationDao.findStationByList(List.of(1L, 3L, 4L))).thenReturn(stations);
        when(pathFindStrategy.findPathInfo(1L, 4L, sections)).thenReturn(
            new PathInfo(List.of(1L, 3L, 4L), Set.of(1L, 2L), 7));
        when(fareCalculator.calculate(any())).thenReturn(1450);
    }

    @Test
    @DisplayName("최단 경로의 정보를 조회한다.")
    public void getDijkstraShortestPath() {
        PathFindingRequest pathFindingRequest = new PathFindingRequest(1L, 4L, 25);
        assertAll(
            () -> assertThat(
                pathService.findPathInfo(pathFindingRequest).getPath().stream().map(StationResponse::getId).collect(
                    Collectors.toList())).asList().containsExactly(1L, 3L, 4L),
            () -> assertThat(pathService.findPathInfo(pathFindingRequest).getDistance()).isEqualTo(7),
            () -> assertThat(pathService.findPathInfo(pathFindingRequest).getFare()).isEqualTo(1450)
        );
    }
}
