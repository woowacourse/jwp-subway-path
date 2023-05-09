package subway.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import subway.application.LineService;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.service.dto.LineDto;
import subway.service.dto.SectionCreateDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class LineServiceTest {

    private LineDao lineDao;
    private StationDao stationDao;
    private SectionDao sectionDao;
    private LineService lineService;

    @BeforeEach
    void setUp() {
        lineDao = Mockito.mock(LineDao.class);
        stationDao = Mockito.mock(StationDao.class);
        sectionDao = Mockito.mock(SectionDao.class); // TODO: @MockBean 으로 주입할 수 있도록하기
        lineService = new LineService(lineDao, stationDao, sectionDao);
    }

    @Test
    @DisplayName("초기 생성")
    void save_success() {
        // given
        final LineDto lineDto = new LineDto("디투당선", "bg-gogi-600");
        final SectionCreateDto sectionCreateDto = new SectionCreateDto(10, "디", "투");

        // when
        given(lineDao.insert(any())).willReturn(1L); // TODO : lineDao insert 반환 값 int
        given(stationDao.findIdByName("디")).willReturn(1L);
        given(stationDao.findIdByName("투")).willReturn(2L);
        given(sectionDao.insert(any())).willReturn(1L); // TODO : sectionDao insert 반환 값 int

        // then
        assertThat(lineService.save(lineDto, sectionCreateDto)).isEqualTo(1L);
    }

}
