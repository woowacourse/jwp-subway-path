package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.application.LineService;
import subway.application.response.LineResponse;
import subway.config.ServiceTestConfig;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.application.response.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineServiceTest extends ServiceTestConfig {

    LineService lineService;

    @BeforeEach
    void setUp() {
        lineService = new LineService(lineRepository);
    }

    @Test
    void 노선과_노선에_해당하는_역을_조회한다() {
        // given
        final Long saveUpStationId = stationDao.insert(new StationEntity("잠실"));
        final Long saveDownStationId = stationDao.insert(new StationEntity("잠실나루"));
        final Long saveLineId = lineDao.insert("2", "초록");

        sectionDao.insert(new SectionEntity(10, true, saveUpStationId, saveDownStationId, saveLineId));

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
        final Long saveUpStationId = stationDao.insert(new StationEntity("잠실"));
        final Long saveDownStationId = stationDao.insert(new StationEntity("잠실나루"));
        final Long saveLineId = lineDao.insert("2", "초록");

        sectionDao.insert(new SectionEntity(10, true, saveUpStationId, saveDownStationId, saveLineId));

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
