package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.application.request.CreateSectionRequest;
import subway.application.response.SectionResponse;
import subway.application.response.StationResponse;
import subway.config.ServiceTestConfig;
import subway.dao.entity.StationEntity;

import static org.assertj.core.api.Assertions.assertThat;

class SectionServiceTest extends ServiceTestConfig {

    SectionService sectionService;

    @BeforeEach
    void setUp() {
        sectionService = new SectionService(sectionRepository);
    }

    @Test
    void 구간을_저장한다() {
        // given
        final CreateSectionRequest request = new CreateSectionRequest("잠실", "잠실새내", 1L, 10);

        // when
        final Long saveSectionId = sectionService.saveSection(request);

        // then
        assertThat(saveSectionId)
                .isNotNull()
                .isNotZero();
    }

    @Test
    void 구간을_조회한다() {
        // given
        final Long upStationId = stationDao.insert(new StationEntity("루카"));
        final Long downStationId = stationDao.insert(new StationEntity("헤나"));

        final CreateSectionRequest request = new CreateSectionRequest("잠실", "잠실새내", 0L, 10);
        final Long saveSectionId = sectionService.saveSection(request);

        // when
        final SectionResponse sectionResponse = sectionService.findBySectionId(saveSectionId);

        // then
        assertThat(sectionResponse)
                .usingRecursiveComparison()
                .isEqualTo(
                        SectionResponse.from(
                                saveSectionId,
                                10,
                                new StationResponse(upStationId, "루카"),
                                new StationResponse(downStationId, "헤나")
                        )
                );
    }
}
