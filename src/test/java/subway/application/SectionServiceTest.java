package subway.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static subway.TestSource.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.ui.dto.DeleteSectionRequest;
import subway.ui.dto.PostSectionRequest;
import subway.ui.dto.SectionResponse;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SectionServiceTest {

    @Mock
    StationDao stationDao;
    @Mock
    LineDao lineDao;
    @Mock
    SectionDao sectionDao;
    @InjectMocks
    SectionService sectionService;

    @Test
    void 노선의_중간에_구간을_저장한다() {
        // given
        // 잠실 - 10 - 천호
        when(stationDao.findById(jamsil.getId())).thenReturn(Optional.of(jamsil));
        when(stationDao.findById(mongchon.getId())).thenReturn(Optional.of(mongchon));
        when(lineDao.findById(pink.getId())).thenReturn(pink);
        when(sectionDao.findAllByLineId(cheonhoJamsil10.getId())).thenReturn(List.of(cheonhoJamsil10));
        when(sectionDao.insertAll(any())).thenReturn(List.of(new Section(2L, mongchon, jamsil, pink, 5)));

        // when
        // 잠실 - 5 - 몽촌 - 5 - 천호
        PostSectionRequest request = new PostSectionRequest(mongchon.getId(), jamsil.getId(), pink.getId(), 5);
        SectionResponse sectionResponse = sectionService.saveSection(request);

        // then
        assertThat(sectionResponse.getId()).isEqualTo(2L);
    }

    @Test
    void 노선에_존재하지_않는_역으로_구간을_저장하면_예외가_발생한다() {
        // given
        // 잠실 - 10 - 천호
        when(stationDao.findById(3L)).thenReturn(Optional.empty());
        when(sectionDao.findAllByLineId(cheonhoJamsil10.getId())).thenReturn(List.of(cheonhoJamsil10));

        // when
        PostSectionRequest request = new PostSectionRequest(3L, jamsil.getId(), pink.getId(), 5);

        // then
        assertThatThrownBy(() -> sectionService.saveSection(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("해당 노선에 stationId = " + 3L + " 인 역이 존재하지 않습니다");
    }

    @Test
    void 노선의_마지막이_아닌_구간을_삭제한다() {
        // given
        // 장지 - 10 - 잠실 - 10 - 천호
        Station cheonho = new Station(1L, "천호");
        Station jamsil = new Station(2L, "잠실");
        Station jangji = new Station(3L, "장지");
        Line line8 = new Line(1L, "8호선", "pink");
        Section cheonhoJamsil10 = new Section(cheonho, jamsil, line8, 10);
        Section jamsilJangji10 = new Section(jamsil, jangji, line8, 10);

        when(sectionDao.findAllByLineId(line8.getId())).thenReturn(List.of(cheonhoJamsil10, jamsilJangji10));

        // when
        DeleteSectionRequest request = new DeleteSectionRequest(1L, 2L);

        // then
        assertDoesNotThrow(() -> sectionService.deleteSection(request));
    }

    @Test
    void 노선의_마지막_남은_구간을_삭제한다() {
        // given
        // 잠실 - 10 - 천호
        when(sectionDao.findAllByLineId(pink.getId())).thenReturn(List.of(cheonhoJamsil10));

        // when
        DeleteSectionRequest request = new DeleteSectionRequest(pink.getId(), jamsil.getId());

        // then
        assertDoesNotThrow(() -> sectionService.deleteSection(request));
        verify(lineDao, times(1)).deleteById(any());
    }

    @Test
    void 삭제할_역이_노선에_존재하지_않으면_예외가_발생한다() {
        // given
        // 잠실 - 10 - 천호
        when(sectionDao.findAllByLineId(cheonhoJamsil10.getId())).thenReturn(List.of(cheonhoJamsil10));

        // when
        DeleteSectionRequest request = new DeleteSectionRequest(pink.getId(), -1L);

        // then
        assertThatThrownBy(() -> sectionService.deleteSection(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("해당 노선에 stationId = " + -1L + " 인 역이 존재하지 않습니다");
    }
}
