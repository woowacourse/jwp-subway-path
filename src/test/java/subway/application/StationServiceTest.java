package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import subway.application.request.CreateSectionRequest;
import subway.application.request.CreateStationRequest;
import subway.application.response.StationResponse;
import subway.config.ServiceTestConfig;
import subway.dao.entity.StationEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;

@DisplayNameGeneration(ReplaceUnderscores.class)
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
}
