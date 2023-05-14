//package subway.service;
//
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.mockito.BDDMockito.given;
//
//import common.exception.NoSuchLineException;
//import common.exception.NoSuchStationException;
//import java.util.List;
//import java.util.Optional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import subway.adapter.in.web.section.dto.AddStationToLineRequest;
//import subway.domain.Line;
//import subway.domain.Section;
//import subway.fixture.LineFixture.이호선;
//import subway.fixture.SectionFixture.이호선_역삼_삼성_3;
//import subway.fixture.StationFixture.삼성역;
//import subway.fixture.StationFixture.역삼역;
//import subway.fixture.StationFixture.잠실역;
//
//class SectionServiceTest {
//
//    private LineRepository lineRepository;
//    private StationRepository stationRepository;
//    private SectionService sectionService;
//
//    @BeforeEach
//    void setUp() {
//        lineRepository = Mockito.mock(LineRepository.class);
//        stationRepository = Mockito.mock(StationRepository.class);
//        sectionService = new SectionService(lineRepository, stationRepository);
//    }
//
//    @Nested
//    class 섹션_생성시_ {
//
//        @Test
//        void 라인아이디에_해당하는_라인이_없을시_예외() {
//            // given
//            long lineId = 1L;
//            given(lineRepository.findById(lineId))
//                    .willReturn(Optional.empty());
//
//            // when then
//            assertThatThrownBy(() -> sectionService.createSection(lineId, 이호선_역삼_삼성_3.REQUEST))
//                    .isInstanceOf(NoSuchLineException.class);
//        }
//
//        @Test
//        void 상행역_아이디에_해당하는_역이_없을시_예외() {
//            // given
//            long lineId = 1L;
//            given(lineRepository.findById(lineId))
//                    .willReturn(Optional.of(이호선.LINE));
//
//            AddStationToLineRequest request = 이호선_역삼_삼성_3.REQUEST;
//            given(stationRepository.findById(request.getUpStationId()))
//                    .willReturn(Optional.empty());
//
//            // when then
//            assertThatThrownBy(() -> sectionService.createSection(lineId, request))
//                    .isInstanceOf(NoSuchStationException.class);
//        }
//
//        @Test
//        void 하행역_아이디에_해당하는_역이_없을시_예외() {
//            // given
//            long lineId = 1L;
//            given(lineRepository.findById(lineId))
//                    .willReturn(Optional.of(이호선.LINE));
//
//            AddStationToLineRequest request = 이호선_역삼_삼성_3.REQUEST;
//            given(stationRepository.findById(request.getDownStationId()))
//                    .willReturn(Optional.empty());
//
//            // when then
//            assertThatThrownBy(() -> sectionService.createSection(lineId, request))
//                    .isInstanceOf(NoSuchStationException.class);
//        }
//
//        @Test
//        void 정상_동작() {
//            // given
//            long lineId = 1L;
//            given(lineRepository.findById(lineId))
//                    .willReturn(Optional.of(이호선.LINE));
//
//            AddStationToLineRequest request = 이호선_역삼_삼성_3.REQUEST;
//            given(stationRepository.findById(request.getUpStationId()))
//                    .willReturn(Optional.of(역삼역.STATION));
//            given(stationRepository.findById(request.getDownStationId()))
//                    .willReturn(Optional.of(삼성역.STATION));
//
//            // when then
//            assertDoesNotThrow(() -> sectionService.createSection(lineId, request));
//        }
//    }
//
//    @Nested
//    class 섹션_제거시_ {
//
//        @Test
//        void 라인아이디에_해당하는_라인이_없을시_예외() {
//            // given
//            long lineId = 1L;
//            given(lineRepository.findById(lineId))
//                    .willReturn(Optional.empty());
//
//            // when then
//            assertThatThrownBy(() -> sectionService.deleteSection(lineId, 1L))
//                    .isInstanceOf(NoSuchLineException.class);
//        }
//
//        @Test
//        void 상행역_아이디에_해당하는_역이_없을시_예외() {
//            // given
//            long lineId = 1L;
//            given(lineRepository.findById(lineId))
//                    .willReturn(Optional.of(이호선.LINE));
//
//            long stationId = 2L;
//            given(stationRepository.findById(stationId))
//                    .willReturn(Optional.empty());
//
//            // when then
//            assertThatThrownBy(() -> sectionService.deleteSection(lineId, stationId))
//                    .isInstanceOf(NoSuchStationException.class);
//        }
//
//        @Test
//        void 정상_동작() {
//            // given
//            long lineId = 1L;
//            given(lineRepository.findById(lineId))
//                    .willReturn(Optional.of(new Line(lineId, "이호선", "GREEN", List.of(new Section(역삼역.STATION,
//                            잠실역.STATION, 10)))));
//
//            long stationId = 2L;
//            given(stationRepository.findById(stationId))
//                    .willReturn(Optional.of(역삼역.STATION));
//
//            // when then
//            assertDoesNotThrow(() -> sectionService.deleteSection(lineId, stationId));
//        }
//    }
//}
