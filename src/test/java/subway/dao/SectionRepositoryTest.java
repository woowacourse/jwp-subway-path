package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static subway.integration.TestFixture.잠실나루역;
import static subway.integration.TestFixture.잠실나루역_잠실역;
import static subway.integration.TestFixture.잠실새내역;
import static subway.integration.TestFixture.잠실새내역_종합운동장역;
import static subway.integration.TestFixture.잠실역;
import static subway.integration.TestFixture.잠실역_잠실새내역;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.domain.Section;
import subway.dto.SectionDto;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class SectionRepositoryTest {

    private static final long LINE_ID = 1L;
    private static final long SECTION_ID = 1L;

    @InjectMocks
    private SectionRepository sectionRepository;
    @Mock
    private SectionDao sectionDao;
    @Mock
    private StationDao stationDao;

    @DisplayName("구간을 저장한다")
    @Test
    void saveSection() {
        doReturn(SECTION_ID).when(sectionDao).insert(any(SectionDto.class));

        Long savedId = sectionRepository.save(LINE_ID, 잠실역_잠실새내역);

        assertThat(savedId).isEqualTo(SECTION_ID);
        verify(sectionDao, times(1)).insert(any(SectionDto.class));
    }

    @DisplayName("구간들을 모두 저장한다")
    @Test
    void saveAllSections() {
        doReturn(2).when(sectionDao).deleteAllByLineId(LINE_ID);
        doNothing().when(sectionDao).insertAll(anyList());
        List<Section> sectionsToSave = List.of(잠실나루역_잠실역, 잠실역_잠실새내역, 잠실새내역_종합운동장역);

        sectionRepository.saveAll(LINE_ID, sectionsToSave);

        verify(sectionDao, times(1)).deleteAllByLineId(eq(LINE_ID));
        verify(sectionDao, times(1)).insertAll(anyList());
    }

    @DisplayName("호선에 맞는 구간을 꺼내온다")
    @Test
    void findAllByLineId() {
        doReturn(잠실새내역).when(stationDao).findById(잠실새내역.getId());
        doReturn(잠실역).when(stationDao).findById(잠실역.getId());
        doReturn(잠실나루역).when(stationDao).findById(잠실나루역.getId());
        doReturn(List.of(
                new SectionDto(LINE_ID, 잠실나루역.getId(), 잠실역.getId(), 10),
                new SectionDto(LINE_ID, 잠실역.getId(), 잠실새내역.getId(), 5)
        )).when(sectionDao).findAllByLineId(LINE_ID);

        List<Section> sections = sectionRepository.findAllBy(LINE_ID);

        assertThat(sections).containsExactly(
                잠실나루역_잠실역,
                잠실역_잠실새내역
        );
    }

    @DisplayName("호선에 맞는 구간들을 제거한다")
    @Test
    void deleteAllByLineId() {
        doReturn(1).when(sectionDao).deleteAllByLineId(any());

        sectionRepository.deleteAllBy(LINE_ID);

        verify(sectionDao, times(1)).deleteAllByLineId(eq(LINE_ID));
    }
}
