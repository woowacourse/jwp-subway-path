package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.SectionDao;
import subway.domain.Section;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @InjectMocks
    private SectionService sectionService;

    @Mock
    private SectionDao sectionDao;

    /*
      기존:
      후: a-b
    * */
    @Test
    void 노선에_역은_존재하지만_아무_구간이_없을_때_새로운_구간을_추가한다() {
        // given
        final Long lineId = 1L;
        final Long stationId1 = 1L;
        final Long stationId2 = 2L;
        int distance = 3;

        final Section request = Section.of(lineId, stationId1, stationId2, distance);
        when(sectionDao.insert(request)).thenReturn(1L);

        // when
        final Long id = sectionService.saveSection(request);

        // then
        assertThat(id).isPositive();
    }


    /*
      기존: a-b   b-c
      후: a-b-c
    * */
    @Test
    void 노선에_이미_구간이_있을때_맨_뒤에_새로운_구간을_추가한다() {

    }

    /*
      기존: a-b   c-a
      후: c-a-b
    * */
    @Test
    void 노선에_이미_구간이_있을때_맨_앞에_새로운_구간을_추가한다() {

    }

    /*
      기존: a-b   a-c
      후: a-c-b
    * */
    @Test
    void 노선에_이미_구간이_있을때_중간에_새로운_역을_추가한다() {

    }
}
