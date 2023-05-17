package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.application.request.CreateSectionRequest;
import subway.application.request.CreateStationRequest;
import subway.application.request.DeleteStationRequest;
import subway.application.response.StationResponse;
import subway.config.ServiceTestConfig;
import subway.dao.entity.StationEntity;
import subway.domain.vo.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class StationServiceTest extends ServiceTestConfig {

    StationService stationService;

    @BeforeEach
    void setUp() {
        stationService = new StationService(stationRepository, sectionRepository, lineRepository);
    }

    @Test
    void 역을_등록한다() {
        // given
        final CreateStationRequest 잠실역_요청 = new CreateStationRequest("잠실");

        // when
        final Long 잠실역_식별자값 = stationService.saveStation(잠실역_요청);

        // then
        assertThat(잠실역_식별자값)
                .isNotNull()
                .isNotZero();
    }

    @Test
    void 노선에_새로운_구간을_추가할_때_등록되지_않은_새로운_역일_경우_역을_저장한다() {
        // given
        final Long 노선1_식별자값 = lineDao.insert("1", "파랑");

        final CreateSectionRequest 구간_요청
                = new CreateSectionRequest("창동", "녹천", 노선1_식별자값, 10);

        // when
        stationService.saveSection(구간_요청);


        final Line 노선 = lineRepository.findByLineId(노선1_식별자값);
        final Section 구간 = 노선.getSections().get(0);

        final String 노선명 = 노선.getNameValue();
        final String 노선_색상 = 노선.getColorValue();

        final String 구간의_상행역 = 구간.getUpStation().getNameValue();
        final String 구간의_하행역 = 구간.getDownStation().getNameValue();

        // then

        assertAll(
                () -> assertThat(구간의_상행역).isEqualTo("창동"),
                () -> assertThat(구간의_하행역).isEqualTo("녹천"),
                () -> assertThat(노선명).isEqualTo("1"),
                () -> assertThat(노선_색상).isEqualTo("파랑")
        );
    }

    @Test
    void 노선에_중복되는_구간을_추가할_경우_예외가_발생한다() {
        // given
        final Long 노선2_식별자값 = lineDao.insert("2", "초록");
        final Long 잠실_식별자값 = stationDao.insert(new StationEntity("잠실"));
        final Long 잠실새내_식별자값 = stationDao.insert(new StationEntity("잠실새내"));

        sectionDao.insert(잠실_식별자값, 잠실새내_식별자값, 노선2_식별자값, false, 10);

        // when
        final CreateSectionRequest 구간_요청
                = new CreateSectionRequest("잠실", "잠실새내", 노선2_식별자값, 10);

        // then
        assertThatThrownBy(() -> stationService.saveSection(구간_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 노선에_구간이_하나_이상_존재할_때_상행역_하행역_모두_노선에_없는_새로운_구간을_추가할_경우_예외가_발생한다() {
        // given
        final Long 노선2_식별자값 = lineDao.insert("2", "초록");
        final Long 잠실_식별자값 = stationDao.insert(new StationEntity("잠실"));
        final Long 잠실새내_식별자값 = stationDao.insert(new StationEntity("잠실새내"));

        sectionDao.insert(잠실_식별자값, 잠실새내_식별자값, 노선2_식별자값, false, 10);

        // when
        final CreateSectionRequest 구간_요청
                = new CreateSectionRequest("창동", "녹천", 노선2_식별자값, 10);

        // then
        assertThatThrownBy(() -> stationService.saveSection(구간_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 역_식별자값으로_역을_조회한다() {
        // given
        final CreateStationRequest 잠실_요청 = new CreateStationRequest("잠실");
        final Long 잠실역_식별자값 = stationService.saveStation(잠실_요청);

        // when
        final StationResponse 역_조회_응답 = stationService.findByStationId(잠실역_식별자값);

        // then
        assertThat(역_조회_응답)
                .usingRecursiveComparison()
                .isEqualTo(new StationResponse(
                        잠실역_식별자값, "잠실"
                ));
    }

    @Test
    void 단_하나의_구간이_있을_때_역명과_노선명으로_역을_삭제한다() {
        // given
        final Long 노선1_식별자값 = lineDao.insert("1", "파랑");

        final CreateSectionRequest 구간_요청
                = new CreateSectionRequest("창동", "녹천", 노선1_식별자값, 10);
        final DeleteStationRequest 역_삭제_요청 = new DeleteStationRequest("창동", "1");

        stationService.saveSection(구간_요청);

        // when
        stationService.deleteByStationNameAndLineName(역_삭제_요청);

        final Line 노선 = lineRepository.findByLineId(노선1_식별자값);
        final List<Station> 전체_역_목록 = 노선.getAllStations();
        final List<Section> 전체_구간_목록 = 노선.getSections();

        // then
        assertAll(
                () -> assertThat(전체_역_목록).isEmpty(),
                () -> assertThat(전체_구간_목록).isEmpty()
        );
    }

    @Test
    void 여러_구간이_있을_때_역명과_노선명으로_역을_삭제한다() {
        // given
        final Distance 거리20 = Distance.from(20);
        final Long 노선1_식별자값 = lineDao.insert("1", "파랑");

        final CreateSectionRequest 방학_창동_요청
                = new CreateSectionRequest("방학", "창동", 노선1_식별자값, 10);
        final CreateSectionRequest 창동_녹천_요청
                = new CreateSectionRequest("창동", "녹천", 노선1_식별자값, 10);

        stationService.saveSection(방학_창동_요청);
        stationService.saveSection(창동_녹천_요청);

        final DeleteStationRequest 역_삭제_요청 = new DeleteStationRequest("창동", "1");

        // when
        stationService.deleteByStationNameAndLineName(역_삭제_요청);

        final Line 노선 = lineRepository.findByLineId(노선1_식별자값);
        final List<Station> 전체_역_목록 = 노선.getAllStations();
        final List<Section> 전체_구간_목록 = 노선.getSections();

        // then
        assertAll(
                () -> assertThat(전체_역_목록)
                        .containsExactly(new Station("방학"), new Station("녹천")),
                () -> assertThat(전체_구간_목록)
                        .containsExactly(new Section(거리20, true, new Station("방학"), new Station("녹천")))
        );
    }
}
