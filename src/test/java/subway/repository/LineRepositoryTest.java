package subway.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.config.RepositoryTestConfig;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineRepositoryTest extends RepositoryTestConfig {

    LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        lineRepository = new LineRepository(lineDaoV2, sectionDaoV2, stationDaoV2);
    }

    @Test
    void 노선에_해당하는_구간을_조회한다() {
        // given
        final Long saveUpStationId = stationDaoV2.insert(new StationEntity("잠실"));
        final Long saveDownStationId = stationDaoV2.insert(new StationEntity("잠실나루"));
        final Long saveLineId = lineDaoV2.insert("2", "초록");
        sectionDaoV2.insert(new SectionEntity(10, true, saveUpStationId, saveDownStationId, saveLineId));

        // when
        final LineDomain findLine = lineRepository.findByLineId(saveLineId);

        // then
        assertThat(findLine)
                .isEqualTo(new LineDomain(saveLineId, "2", "초록",
                        SectionsDomain.from(List.of(
                                SectionDomain.from(
                                        saveLineId,
                                        new Distance(10),
                                        true,
                                        new StationDomain(saveUpStationId, "잠실"),
                                        new StationDomain(saveDownStationId, "잠실나루")
                                )
                        ))
                ));
    }

    @Test
    void 모든_노선과_구간을_조회한다() {
        // given
        final Long saveUpStationId = stationDaoV2.insert(new StationEntity("잠실"));
        final Long saveDownStationId = stationDaoV2.insert(new StationEntity("잠실나루"));
        final Long saveLineId = lineRepository.saveLine("2", "초록");

        sectionDaoV2.insert(new SectionEntity(10, true, saveUpStationId, saveDownStationId, saveLineId));

        // when
        final List<LineDomain> findLines = lineRepository.findAll();

        // then
        assertThat(findLines)
                .contains(new LineDomain(saveLineId, "2", "초록",
                                SectionsDomain.from(List.of(
                                        SectionDomain.from(
                                                saveLineId,
                                                new Distance(10),
                                                true,
                                                new StationDomain(saveUpStationId, "잠실"),
                                                new StationDomain(saveDownStationId, "잠실나루")
                                        )
                                ))
                        )
                );
    }
}
