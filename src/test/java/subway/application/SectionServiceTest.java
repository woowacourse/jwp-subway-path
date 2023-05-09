package subway.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.InitialStationsAddRequest;
import subway.dto.SectionResponse;
import subway.exception.DomainException;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {
    @InjectMocks
    private SectionService sectionService;
    @Mock
    private LineDao lineDao;
    @Mock
    private StationDao stationDao;
    @Mock
    private SectionDao sectionDao;

    @Test
    @DisplayName("구간이 등록되지 않은 라인에 구간을 추가한다.")
    void addSectionTest() {
        Station station1 = new Station(1L, "잠실");
        Station station2 = new Station(2L, "잠실새내");
        Line line = new Line(1L, "2호선", "초록");
        when(sectionDao.findAllSectionByLineId(1L)).thenReturn(new ArrayList<>());
        when(stationDao.findById(1L)).thenReturn(station1);
        when(stationDao.findById(2L)).thenReturn(station2);
        when(lineDao.findById(1L)).thenReturn(line);
        when(sectionDao.insert(new Section(station1, station2, line, new Distance(2)))).thenReturn(1L);

        SectionResponse result = sectionService.addSection(new InitialStationsAddRequest(1L, 1L, 2L, 2));

        Assertions.assertAll(
            () -> assertThat(result.getId()).isEqualTo(1L),
            () -> assertThat(result.getDistance()).isEqualTo(2),
            () -> assertThat(result.getLineId()).isEqualTo(1L),
            () -> assertThat(result.getFirstStationId()).isEqualTo(1L),
            () -> assertThat(result.getSecondStationId()).isEqualTo(2L)
        );
    }

    @Test
    @DisplayName("구간이 등록된 라인에 구간을 추가하면 에러를 반환한다.")
    void addSectionFailTest() {
        Station station1 = new Station(1L, "잠실");
        Station station2 = new Station(2L, "잠실새내");
        Line line = new Line(1L, "2호선", "초록");
        List<Section> sections = List.of(new Section(station1, station2, line, new Distance(2)),
            new Section(station2, station1, line, new Distance(2)));
        when(sectionDao.findAllSectionByLineId(1L)).thenReturn(sections);

        assertThatThrownBy(() -> sectionService.addSection(new InitialStationsAddRequest(1L, 1L, 2L, 2))).isInstanceOf(
            DomainException.class);
    }
}
