package subway.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.AbstractMap;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.fee.FeeStrategy;
import subway.domain.path.PathFindStrategy;
import subway.domain.subway.Section;
import subway.domain.subway.Station;
import subway.dto.PathFindingRequest;
import subway.dto.StationResponse;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
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
    private StationDao stationDao;
    @Mock
    private SectionDao sectionDao;
    @Mock
    private FeeStrategy feeStrategy;
    @Mock
    private PathFindStrategy pathFindStrategy;

    @BeforeEach
    void setUp() {
        when(sectionDao.findAll()).thenReturn(sections);
        when(stationDao.findStationByList(List.of(1L, 3L, 4L))).thenReturn(stations);
        when(pathFindStrategy.findPathAndTotalDistance(1L, 4L, sections)).thenReturn(
            new AbstractMap.SimpleEntry<>(List.of(1L, 3L, 4L), 7));
        when(feeStrategy.calculate(7)).thenReturn(1250);
    }

    @Test
    @DisplayName("최단 경로의 정보를 조회한다.")
    public void getDijkstraShortestPath() {
        PathFindingRequest pathFindingRequest = new PathFindingRequest(1L, 4L);
        assertAll(
            () -> assertThat(
                pathService.findPathInfo(pathFindingRequest).getPath().stream().map(StationResponse::getId).collect(
                    Collectors.toList())).asList().containsExactly(1L, 3L, 4L),
            () -> assertThat(pathService.findPathInfo(pathFindingRequest).getDistance()).isEqualTo(7),
            () -> assertThat(pathService.findPathInfo(pathFindingRequest).getFee()).isEqualTo(1250)
        );
    }
}
