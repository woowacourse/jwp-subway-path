package subway.application;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.SectionDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.SectionRequest;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @Mock
    private SectionDao sectionDao;

    @InjectMocks
    private SectionService sectionService;

    @Test
    void 빈_노선에_섹션을_추가한다() {
        //given
        Line line = new Line(1L, "2호선", "Green");
        SectionRequest sectionRequest = new SectionRequest(1L, 2L, 10);
        Sections emptySections = new Sections();

        // when
        when(sectionDao.findSectionsByLineId(1L)).thenReturn(Optional.empty());
        sectionService.addSection(line.getId(), sectionRequest);

        // then
        verify(sectionDao, atLeastOnce()).insert(sectionRequest.getFromId(), sectionRequest.getToId(),
                sectionRequest.getDistance(), line.getId());
    }

    @Test
    void 노선_중간에_섹션을_추가한다() {
        // given
        Line line = new Line(1L, "2호선", "Green");
        SectionRequest sectionRequest = new SectionRequest(2L, 5L, 3);
        Sections sections = new Sections(List.of(
                new Section(new Station(1L, "후추"), new Station(2L, "디노"), 6),
                new Section(new Station(2L, "디노"), new Station(3L, "조앤"), 7)));

        // when
        when(sectionDao.findSectionsByLineId(1L)).thenReturn(Optional.of(sections));
        sectionService.addSection(line.getId(), sectionRequest);

        // then
        verify(sectionDao, atLeast(2)).insert(any(Long.class), any(Long.class), any(Integer.class), any(Long.class));
        verify(sectionDao, atLeastOnce()).deleteSectionBySectionInfo(any(Long.class), any(Section.class));
        InOrder inOrder = inOrder(sectionDao);
        inOrder.verify(sectionDao).deleteSectionBySectionInfo(any(Long.class), any(Section.class));
        inOrder.verify(sectionDao).insert(sectionRequest.getFromId(), sectionRequest.getToId(),
                sectionRequest.getDistance(), line.getId());
        inOrder.verify(sectionDao).insert(any(Long.class), any(Long.class), any(Integer.class), any(Long.class));

    }

    @Test
    void 노선_끝에_섹션을_추가한다() {
        //given
        Line line = new Line(1L, "2호선", "Green");
        SectionRequest sectionRequest = new SectionRequest(3L, 5L, 3);
        Sections sections = new Sections(List.of(
                new Section(new Station(1L, "후추"), new Station(2L, "디노"), 6),
                new Section(new Station(2L, "디노"), new Station(3L, "조앤"), 7)));

        // when
        when(sectionDao.findSectionsByLineId(line.getId())).thenReturn(Optional.of(sections));
        sectionService.addSection(line.getId(), sectionRequest);

        // then
        verify(sectionDao, atLeastOnce()).insert(sectionRequest.getFromId(), sectionRequest.getToId(),
                sectionRequest.getDistance(), line.getId());
    }

    @Test
    void 노선_중간에_위치한_역을_삭제한다() {
        //given
        Line line = new Line(1L, "2호선", "Green");
        Sections sections = new Sections(List.of(
                new Section(new Station(1L, "후추"), new Station(2L, "디노"), 6),
                new Section(new Station(2L, "디노"), new Station(3L, "조앤"), 7)
        ));

        // when
        when(sectionDao.findSectionsByStationInfo(line.getId(), 2L)).thenReturn(sections);
        sectionService.deleteStationById(line.getId(), 2L);

        // then
        InOrder inOrder = inOrder(sectionDao);
        inOrder.verify(sectionDao, atLeastOnce())
                .insert(any(Long.class), any(Long.class), any(Integer.class), any(Long.class));
        inOrder.verify(sectionDao, atLeastOnce()).deleteSectionByStationId(any(Long.class), any(Long.class));
    }

    @Test
    void 노선_끝에_위치한_역을_삭제한다() {
        //given
        Line line = new Line(1L, "2호선", "Green");
        Sections sections = new Sections(List.of(
                new Section(new Station(1L, "후추"), new Station(2L, "디노"), 6),
                new Section(new Station(2L, "디노"), new Station(3L, "조앤"), 7)
        ));

        // when
        when(sectionDao.findSectionsByStationInfo(line.getId(), 3L)).thenReturn(sections);
        sectionService.deleteStationById(line.getId(), 3L);

        // then
        verify(sectionDao, atLeastOnce()).deleteSectionByStationId(any(Long.class), any(Long.class));
    }

    @Test
    void 노선에_존재하지_않는_역을_삭제한다() {
        // given
        Long lineId = 1L;
        Long stationId = 2L;
        Sections emptySections = new Sections(Collections.emptyList());
        when(sectionDao.findSectionsByStationInfo(lineId, stationId)).thenReturn(emptySections);

        // when
        assertThrows(IllegalArgumentException.class, () -> {
            sectionService.deleteStationById(lineId, stationId);
        });

        // then
        verify(sectionDao, never()).deleteSectionByStationId(anyLong(), anyLong());
    }

    @Test
    void 특정_노선에_해당하는_역들을_반환한다() {
        // given
        Line line = new Line(1L, "2호선", "Green");
        Sections sections = new Sections(List.of(
                new Section(new Station(1L, "후추"), new Station(2L, "디노"), 6),
                new Section(new Station(2L, "디노"), new Station(3L, "조앤"), 7),
                new Section(new Station(3L, "조앤"), new Station(4L, "로운"), 8)
        ));

        // when
        when(sectionDao.findSectionsByLineId(line.getId())).thenReturn(Optional.of(sections));
        sectionService.showStations(line);

        // then
        verify(sectionDao, atLeastOnce()).findSectionsByLineId(line.getId());
    }

}
