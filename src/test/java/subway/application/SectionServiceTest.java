package subway.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import subway.dto.InitialSectionAddRequest;
import subway.dto.SectionAddRequest;
import subway.dto.SectionAddResponse;
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
    void addInitialSectionTest() {
        Long station1Id = 1L;
        Long station2Id = 2L;
        Long lineId = 1L;
        when(sectionDao.findAllSectionByLineId(1L)).thenReturn(new ArrayList<>());
        when(sectionDao.insert(new Section(null, station1Id, station2Id, lineId, 2))).thenReturn(1L);

        SectionAddResponse result = sectionService.addSection(new InitialSectionAddRequest(1L, 1L, 2L, 2));

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("구간이 등록된 라인에 구간을 추가하면 에러를 반환한다.")
    void addInitialSectionFailTest() {
        Long station1Id = 1L;
        Long station2Id = 2L;
        Long lineId = 1L;
        List<Section> sections = List.of(new Section(1L, station1Id, station2Id, lineId, 2));
        when(sectionDao.findAllSectionByLineId(1L)).thenReturn(sections);

        assertThatThrownBy(() -> sectionService.addSection(new InitialSectionAddRequest(1L, 1L, 2L, 2))).isInstanceOf(
            DomainException.class);
    }

    @Test
    @DisplayName("구간이 등록된 라인에 구간을 추가한다. - 구간 사이에 역을 넣는다.")
    void addSectionBetweenTest() {
        Long station1Id = 1L;
        Long station2Id = 2L;
        Long newStationId = 3L;
        Long lineId = 1L;
        Section section = new Section(1L, station1Id, station2Id, lineId, 10);
        SectionAddRequest sectionAddRequest = new SectionAddRequest(lineId, newStationId, station1Id, station2Id, 3);
        when(sectionDao.findAllSectionByLineId(lineId)).thenReturn(List.of(section));

        List<SectionAddResponse> result = sectionService.addSection(sectionAddRequest);

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("구간이 등록된 라인에 구간을 추가한다. - 종점 다음에 역을 넣는다.")
    void addSectionTest() {
        Long station1Id = 1L;
        Long station2Id = 2L;
        Long newStationId = 3L;
        Long lineId = 1L;
        Section section = new Section(1L, station1Id, station2Id, lineId, 10);
        SectionAddRequest sectionAddRequest = new SectionAddRequest(lineId, newStationId, station1Id, null, 3);
        when(sectionDao.findAllSectionByLineId(lineId)).thenReturn(List.of(section));

        List<SectionAddResponse> result = sectionService.addSection(sectionAddRequest);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("구간이 등록되지 않은 라인에 구간을 추가하면, 예외를 발생시킨다.")
    void addSectionFailTest() {
        Long station1Id = 1L;
        Long station2Id = 2L;
        Long newStationId = 3L;
        Long lineId = 1L;
        SectionAddRequest sectionAddRequest = new SectionAddRequest(lineId, newStationId, station1Id, station2Id, 3);
        when(sectionDao.findAllSectionByLineId(lineId)).thenReturn(List.of());

        assertThatThrownBy(() -> sectionService.addSection(sectionAddRequest))
            .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("종점이 아닌곳에 종점을 추가하려 하면, 예외를 발생시킨다.")
    void addSectionFailNotLastStopTest() {
        Long station1Id = 1L;
        Long station2Id = 2L;
        Long station3Id = 3L;
        Long newStationId = 4L;
        Long lineId = 1L;
        SectionAddRequest sectionAddRequest = new SectionAddRequest(lineId, newStationId, station2Id, null, 3);

        Section section1 = new Section(1L, station1Id, station2Id, lineId, 10);
        Section section2 = new Section(2L, station2Id, station3Id, lineId, 10);
        when(sectionDao.findAllSectionByLineId(lineId)).thenReturn(List.of(section1, section2));

        assertThatThrownBy(() -> sectionService.addSection(sectionAddRequest))
            .isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("새로 등록하려는 구간의 길이가 기존의 구간 길이보다 길면 예외를 발생시킨다.")
    void addSectionFailDistanceTest() {
        Long station1Id = 1L;
        Long station2Id = 2L;
        Long newStationId = 3L;
        Long lineId = 1L;
        SectionAddRequest sectionAddRequest = new SectionAddRequest(lineId, newStationId, station1Id, station2Id, 10);

        Section section = new Section(1L, station1Id, station2Id, lineId, 5);
        when(sectionDao.findAllSectionByLineId(lineId)).thenReturn(List.of(section));

        assertThatThrownBy(() -> sectionService.addSection(sectionAddRequest))
            .isInstanceOf(DomainException.class);
    }
}
