package subway.application.v2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.application.response.LineResponse;
import subway.config.ServiceTestConfig;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineServiceV2Test extends ServiceTestConfig {

    LineServiceV2 lineService;

    @BeforeEach
    void setUp() {
        lineService = new LineServiceV2(lineRepository);
    }

    @Test
    void 노선과_노선에_해당하는_역을_조회한다() {
        // given
        final Long saveUpStationId = stationDaoV2.insert(new StationEntity("잠실"));
        final Long saveDownStationId = stationDaoV2.insert(new StationEntity("잠실나루"));
        final Long saveLineId = lineDaoV2.insert("2", "초록");

        sectionDaoV2.insert(new SectionEntity(10, true, saveUpStationId, saveDownStationId, saveLineId));

        // when
        final LineResponse lineResponse = lineService.findByLineId(saveLineId);

        // then
        assertThat(lineResponse)
                .usingRecursiveComparison()
                .isEqualTo(new LineResponse(
                        saveLineId,
                        "2",
                        "초록",
                        List.of(new StationResponse(saveUpStationId, "잠실"),
                                new StationResponse(saveDownStationId, "잠실나루"))
                ));
    }

    @Test
    void 모든_노선과_노선에_해당하는_역을_조회한다() {
        // given
        final Long saveUpStationId = stationDaoV2.insert(new StationEntity("잠실"));
        final Long saveDownStationId = stationDaoV2.insert(new StationEntity("잠실나루"));
        final Long saveLineId = lineDaoV2.insert("2", "초록");

        sectionDaoV2.insert(new SectionEntity(10, true, saveUpStationId, saveDownStationId, saveLineId));

        // when
        final List<LineResponse> lineResponses = lineService.findAll();

        // then
        assertThat(lineResponses)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(new LineResponse(saveLineId, "2", "초록",
                                List.of(
                                        new StationResponse(saveUpStationId, "잠실"),
                                        new StationResponse(saveDownStationId, "잠실나루")
                                )
                        )
                );
    }
}
