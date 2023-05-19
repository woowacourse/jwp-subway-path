package subway.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.subway.Line;
import subway.domain.subway.Section;
import subway.domain.subway.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineStationsResponse;
import subway.dto.StationResponse;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {
    private final List<Station> stations = List.of(
        new Station(1L, "잠실역"),
        new Station(2L, "잠실새내역"),
        new Station(3L, "잠실나루역"),
        new Station(4L, "신림역"),
        new Station(5L, "서울대입구역"),
        new Station(6L, "서울대입구역1"),
        new Station(7L, "서울대입구역2")
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
    private LineService lineService;

    @Mock
    private LineDao lineDao;
    @Mock
    private SectionDao sectionDao;
    @Mock
    private StationDao stationDao;

    @Test
    @DisplayName("노선 정보를 저장한다.")
    void save() {
        when(lineDao.insert(any()))
            .thenReturn(new Line("2호선", "초록"));

        LineResponse lineResponse = lineService.saveLine(new LineRequest());

        assertThat(lineResponse).isNotNull();
    }

    @Test
    @DisplayName("이미 존재하는 노선을 저장하는 경우 예외를 반환한다.")
    void saveFail() {
        when(lineDao.checkExistenceByNameAndColor("1호선", "파랑")).thenReturn(true);
        assertThatThrownBy(() -> lineService.saveLine(new LineRequest("1호선", "파랑")));
    }

    @Test
    @DisplayName("전체 노선들의 역들을 순차적으로 반환한다.")
    void findLineStationsResponses() {
        when(lineDao.findAll()).thenReturn(List.of(new Line(1L, "1호선", "파랑"), new Line(2L, "2호선", "초록")));
        when(sectionDao.findAll()).thenReturn(sections);
        when(stationDao.findAll()).thenReturn(stations);

        List<LineStationsResponse> lineStationsResponses = lineService.findLineStationsResponses();

        assertAll(
            () -> {
                if (lineStationsResponses.get(0).getLine().getId() == 1L) {
                    assertThat(
                        lineStationsResponses.get(0)
                            .getLineStations()
                            .stream()
                            .map(StationResponse::getId)).containsExactly(1L,
                        2L, 3L, 4L, 5L, 6L, 7L);
                }
            },
            () -> {
                if (lineStationsResponses.get(0).getLine().getId() == 2L) {
                    assertThat(
                        lineStationsResponses.get(0)
                            .getLineStations()
                            .stream()
                            .map(StationResponse::getId)).containsExactly(1L, 3L);
                }
            });
    }

    @Test
    @DisplayName("해당 노선의 역들을 순차적으로 반환한다.")
    void findLineStationsResponseById() {
        final List<Section> sections1 = List.of(
            new Section(1L, 1L, 2L, 1L, 5),
            new Section(1L, 2L, 3L, 1L, 5),
            new Section(1L, 3L, 4L, 1L, 5),
            new Section(1L, 4L, 5L, 1L, 5),
            new Section(1L, 5L, 6L, 1L, 5),
            new Section(1L, 6L, 7L, 1L, 50)
        );
        when(lineDao.findById(1L)).thenReturn(new Line(1L, "1호선", "파랑"));
        when(sectionDao.findAllSectionByLineId(1L)).thenReturn(sections1);
        when(stationDao.findAll()).thenReturn(stations);

        LineStationsResponse lineStationsResponses = lineService.findLineStationsResponseById(1L);

        assertAll(
            () -> assertThat(lineStationsResponses.getLine().getId()).isEqualTo(1L),
            () -> assertThat(
                lineStationsResponses.getLineStations()
                    .stream()
                    .map(StationResponse::getId)).containsExactly(1L,
                2L, 3L, 4L, 5L, 6L, 7L)
        );
    }
}
