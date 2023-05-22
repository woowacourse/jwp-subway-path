package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.common.Cost;
import subway.dao.*;
import subway.domain.*;
import subway.domain.path.*;
import subway.dto.PathResponse;
import subway.dto.StationResponse;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @InjectMocks
    private PathService pathService;
    @Mock
    private LineDao lineDao;
    @Mock
    private StationDao stationDao;
    @Mock
    private SectionDao sectionDao;
    @Mock
    private Cost cost;
    @Mock
    private GraphGenerator graph;

    @Test
    @DisplayName("경로를 탐색한다.")
    void findPath() {
        List<Line> lines = List.of(new Line(1L,"0호선"));
        List<Station> stations = List.of(new Station(1L, "인천역"), new Station(2L, "서울역"));
        Station 인천역 = new Station(1L, "인천역");
        Station 서울역 = new Station(2L, "서울역");
        List<Section> sections = List.of(new Section(1L, 인천역, 서울역, new Distance(5)));
        List<StationResponse> stationResponses = List.of(new StationResponse(1L, "인천역"), new StationResponse(2L, "서울역"));
        Graph generate = new JgraphtGraphGenerator().generate();

        when(lineDao.findAll()).thenReturn(lines);
        when(stationDao.findAll()).thenReturn(stations);
        when(sectionDao.findByLineId(any())).thenReturn(sections);
        when(stationDao.findById(1L)).thenReturn(Optional.of(new Station("인천역")));
        when(stationDao.findById(2L)).thenReturn(Optional.of(new Station("서울역")));
        when(graph.generate()).thenReturn(generate);
        when(cost.calculate(anyInt())).thenReturn(5);

        assertThat(pathService.findPath(1L, 2L)).usingRecursiveComparison().isEqualTo(new PathResponse(stationResponses, 5, 5));
    }
}
