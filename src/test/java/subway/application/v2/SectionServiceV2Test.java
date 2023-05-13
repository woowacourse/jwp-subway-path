package subway.application.v2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.application.request.CreateSectionRequest;
import subway.application.response.SectionResponse;
import subway.config.ServiceTestConfig;
import subway.dao.entity.StationEntity;
import subway.dto.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;

class SectionServiceV2Test extends ServiceTestConfig {

    SectionServiceV2 sectionService;

    @BeforeEach
    void setUp() {
        sectionService = new SectionServiceV2(sectionRepository);
    }

    @Test
    void 구간을_저장한다() {
        // given
        final CreateSectionRequest request = new CreateSectionRequest(1L, 1L, 1L, 10);

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
        final Long upStationId = stationDaoV2.insert(new StationEntity("루카"));
        final Long downStationId = stationDaoV2.insert(new StationEntity("헤나"));

        final CreateSectionRequest request = new CreateSectionRequest(upStationId, downStationId, 0L, 10);
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
