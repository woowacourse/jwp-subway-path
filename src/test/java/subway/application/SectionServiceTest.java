package subway.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import subway.ui.dto.SectionRequest;
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
    void 구간을_저장한다() {
        // given
        // 잠실 - 10 - 천호
        Station cheonho = new Station(1L, "천호");
        Station jamsil = new Station(2L, "잠실");
        Station mongchon = new Station(3L, "몽촌토성");
        Line line8 = new Line(1L, "8호선", "pink");
        Section cheonhoJamsil10 = new Section(cheonho, jamsil, line8, 10);

        when(stationDao.findById(2L)).thenReturn(Optional.of(jamsil));
        when(stationDao.findById(3L)).thenReturn(Optional.of(mongchon));
        when(lineDao.findById(1L)).thenReturn(line8);
        when(sectionDao.findAllByLineId(1L)).thenReturn(List.of(cheonhoJamsil10));
        when(sectionDao.insertAll(any())).thenReturn(List.of(new Section(2L, mongchon, jamsil, line8, 5)));

        // when
        SectionRequest request = new SectionRequest(3L, 2L, 1L, 5);
        SectionResponse sectionResponse = sectionService.saveSection(request);

        // then
        assertThat(sectionResponse.getId()).isEqualTo(2L);
    }

    @Test
    void 구간을_삭제한다() {
        // given
        // 잠실 - 10 - 천호
        Station cheonho = new Station(1L, "천호");
        Station jamsil = new Station(2L, "잠실");
        Station mongchon = new Station(3L, "몽촌토성");
        Line line8 = new Line(1L, "8호선", "pink");
        Section cheonhoJamsil10 = new Section(cheonho, jamsil, line8, 10);

        // section에서 findById()로 Section을 찾아온다
        // 해당 Section과 같은 호선의 List<Section>을 모두 가져온다.
        // 해당 Section의 up_station이 다른 구간의 down_station인 경우가 존재하는지 확인한다.
        // 해당 Section의 down_station이 다른 구간의 up_station인 경우가 존재하는지 확인한다.
        // 위의 두 값을 통해 둘 중 하나가 0이라면 구간의 끝을 의미,
        // 해당 section이 구간의 끝이 아니라면 예외가 발생한다.
        //

        // assertDoesNotThrow(() -> sectionService.deleteSection(1L));
    }
}
