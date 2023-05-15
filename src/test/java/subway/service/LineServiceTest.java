//package subway.application;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import subway.dao.LineDao;
//import subway.dao.SectionDao;
//import subway.dao.StationDao;
//import subway.application.dto.LineDto;
//import subway.application.dto.SectionCreateDto;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(MockitoExtension.class)
//class LineServiceTest {
//
//    @Mock
//    private LineDao lineDao;
//    @Mock
//    private StationDao stationDao;
//    @Mock
//    private SectionDao sectionDao;
//    @InjectMocks
//    private LineService lineService;
//
//    @BeforeEach
//    void setUp() {
//        lineService = new LineService(lineDao, stationDao, sectionDao);
//    }
//
//    @Test
//    @DisplayName("초기 생성")
//    void save_success() {
//        // given
//        final LineDto lineDto = new LineDto("디투당선", "bg-gogi-600");
//        final SectionCreateDto sectionCreateDto = new SectionCreateDto(10, "디", "투");
//
//        // when
//        given(lineDao.insert(any())).willReturn(1L); // TODO : lineDao insert 반환 값 int
//        given(stationDao.findByName("디")).willReturn(any());
//        given(stationDao.findByName("투")).willReturn(any());
//        given(sectionDao.insert(any())).willReturn(any()); // TODO : sectionDao insert 반환 값 int
//
//        // then
//        assertThat(lineService.save(lineDto, sectionCreateDto)).isEqualTo(1L);
//    }
//
//}
