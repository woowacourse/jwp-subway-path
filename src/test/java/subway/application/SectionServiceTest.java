package subway.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.AddOneSectionRequest;
import subway.repository.SectionRepository;
import java.util.Collections;

@MockitoSettings
@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @InjectMocks
    private SectionService sectionService;

    @Mock
    private LineDao lineDao;
    @Mock
    private StationDao stationDao;
    @Mock
    private SectionRepository sectionRepository;

    private final Line _2호선 = new Line(1L, "2호선", "초록색");
    private final Station 신림역 = new Station(1L, "신림");
    private final Station 봉천역 = new Station(2L, "봉천");

    @Test
    void 두_역을_추가한다() {
        // given
        when(lineDao.findById(1L)).thenReturn(_2호선);
        when(stationDao.findById(1L)).thenReturn(신림역);
        when(stationDao.findById(2L)).thenReturn(봉천역);
        Distance distance = new Distance(10);
        Section section = new Section(_2호선, 신림역, 봉천역, distance);
        when(sectionRepository.findByLine(new Line(1L, "2호선", "초록색")))
                .thenReturn(new Sections(Collections.emptyList()));

        // when
        AddOneSectionRequest addOneSectionRequest = new AddOneSectionRequest(1L, 2L, 10);
        sectionService.addOneSection(1L, addOneSectionRequest);

        // then
        assertAll(
                () -> verify(lineDao).findById(anyLong()),
                () -> verify(stationDao, times(2)).findById(anyLong()),
                () -> verify(sectionRepository).save(section)
        );
    }
}
