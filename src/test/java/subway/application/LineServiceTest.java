package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.*;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {
    @InjectMocks
    private LineService lineService;
    @Mock
    private LineDao lineDao;
    @Mock
    private StationDao stationDao;
    @Mock
    private SectionDao sectionDao;

    @Test
    @DisplayName("Line을 저장한다.")
    void saveLine() {
        List<Line> lines = List.of(new Line(1L,"0호선"));
        LineRequest lineRequest = new LineRequest("1호선", "인천역", "서울역", 5);
        LineResponse lineResponse = new LineResponse(1L, "1호선",
                List.of(new StationResponse(1L, "인천역"), new StationResponse(2L, "서울역")));
        List<Station> stations = List.of(new Station(1L, "인천역"), new Station(2L, "서울역"));
        Station 인천역 = new Station(1L, "인천역");
        Station 서울역 = new Station(2L, "서울역");
        List<Section> sections = List.of(new Section(1L, 인천역, 서울역, new Distance(5)));

        when(sectionDao.findByLineId(any())).thenReturn(sections);
        when(stationDao.findByName("인천역")).thenReturn(인천역);
        when(stationDao.findByName("서울역")).thenReturn(서울역);
        when(lineDao.findAll()).thenReturn(lines);
        when(lineDao.insert(any())).thenReturn(1L);
        doNothing().when(sectionDao).insertAll(anyLong(),anyList());
        when(stationDao.findAll()).thenReturn(stations);

        assertThat(lineService.saveLine(lineRequest)).usingRecursiveComparison().isEqualTo(lineResponse);
        verify(lineDao, times(1)).findAll();
        verify(stationDao, times(2)).findAll();
        verify(lineDao, times(1)).insert(any());
    }

    @Test
    @DisplayName("모든 Line을 조회한다.")
    void findAllLines() {
        List<Line> line = List.of(new Line(1L, "1호선"));
        Station 인천역 = new Station(1L, "인천역");
        Station 서울역 = new Station(2L, "서울역");
        List<Section> sections = List.of(new Section(1L, 인천역, 서울역, new Distance(5)));
        List<Station> stations = List.of(인천역, 서울역);
        List<LineResponse> lineResponses = List.of(new LineResponse(1L, "1호선",
                List.of(new StationResponse(1L, "인천역"), new StationResponse(2L, "서울역"))));

        when(lineDao.findAll()).thenReturn(line);
        when(sectionDao.findByLineId(any())).thenReturn(sections);
        when(stationDao.findAll()).thenReturn(stations);

        assertThat(lineService.findAllLines()).usingRecursiveComparison().isEqualTo(lineResponses);
        verify(lineDao, times(1)).findAll();
        verify(sectionDao, times(1)).findByLineId(any());
        verify(stationDao, times(1)).findAll();
    }

    @Test
    @DisplayName("lineId로 Line을 조회한다.")
    void findLineResponseById() {
        Line findLine = new Line(1L, "1호선");
        Station 인천역 = new Station(1L, "인천역");
        Station 서울역 = new Station(2L, "서울역");
        List<Section> sections = List.of(new Section(1L, 인천역, 서울역, new Distance(5)));
        List<Station> stations = List.of(인천역, 서울역);
        LineResponse lineResponses = new LineResponse(1L, "1호선",
                List.of(new StationResponse(1L, "인천역"), new StationResponse(2L, "서울역")));

        when(lineDao.findById(any())).thenReturn(Optional.of(findLine));
        when(sectionDao.findByLineId(any())).thenReturn(sections);
        when(stationDao.findAll()).thenReturn(stations);

        assertThat(lineService.findLineResponseById(1L)).usingRecursiveComparison().isEqualTo(lineResponses);
        verify(lineDao, times(1)).findById(any());
        verify(sectionDao, times(1)).findByLineId(any());
        verify(stationDao, times(1)).findAll();
    }
}
