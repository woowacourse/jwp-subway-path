package subway.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.config.RepositoryTestConfig;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.*;
import subway.domain.Line;
import subway.domain.vo.Distance;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LineRepositoryTest extends RepositoryTestConfig {

    LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        lineRepository = new LineRepository(lineDao, sectionDao, stationDao);
    }

    @Test
    void 노선에_해당하는_구간을_조회한다() {
        // given
        final Long saveUpStationId = stationDao.insert(new StationEntity("잠실"));
        final Long saveDownStationId = stationDao.insert(new StationEntity("잠실나루"));
        final Long saveLineId = lineDao.insert("2", "초록");
        sectionDao.insert(new SectionEntity(10, true, saveUpStationId, saveDownStationId, saveLineId));

        // when
        final Line findLine = lineRepository.findByLineId(saveLineId);

        final String lineName = findLine.getNameValue();
        final String lineColor = findLine.getColorValue();
        final List<Section> findSections = findLine.getSections();

        // then
        assertAll(
                () -> assertThat(lineName).isEqualTo("2"),
                () -> assertThat(lineColor).isEqualTo("초록"),
                () -> assertThat(findSections)
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                        .containsExactly(new Section(
                                Distance.from(10),
                                true,
                                new Station(saveUpStationId, "잠실"),
                                new Station(saveDownStationId, "잠실나루")
                        ))
        );
    }

    @Test
    void 노선_이름으로_노선과_해당하는_구간을_조회한다() {
        // given
        final Long saveUpStationId = stationDao.insert(new StationEntity("잠실"));
        final Long saveDownStationId = stationDao.insert(new StationEntity("잠실나루"));
        final Long saveLineId = lineDao.insert("2", "초록");
        sectionDao.insert(new SectionEntity(10, true, saveUpStationId, saveDownStationId, saveLineId));

        // when
        final Line findLine = lineRepository.findByLineName("2");

        final String lineName = findLine.getNameValue();
        final String lineColor = findLine.getColorValue();
        final List<Section> findSections = findLine.getSections();

        // then
        assertAll(
                () -> assertThat(lineName).isEqualTo("2"),
                () -> assertThat(lineColor).isEqualTo("초록"),
                () -> assertThat(findSections)
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                        .containsExactly(new Section(
                                Distance.from(10),
                                true,
                                new Station(saveUpStationId, "잠실"),
                                new Station(saveDownStationId, "잠실나루")
                        ))
        );
    }

    @Test
    void 모든_노선과_구간을_조회한다() {
        // given
        final Long saveUpStationId = stationDao.insert(new StationEntity("잠실"));
        final Long saveDownStationId = stationDao.insert(new StationEntity("잠실나루"));
        final Long saveLineId = lineRepository.saveLine("2", "초록");

        sectionDao.insert(new SectionEntity(10, true, saveUpStationId, saveDownStationId, saveLineId));

        // when
        final List<Line> findLines = lineRepository.findAll();

        final String lineName = findLines.get(0).getNameValue();
        final String lineColor = findLines.get(0).getColorValue();
        final List<Section> findSections = findLines.get(0).getSections();

        // then
        assertAll(
                () -> assertThat(lineName).isEqualTo("2"),
                () -> assertThat(lineColor).isEqualTo("초록"),
                () -> assertThat(findSections)
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                        .containsExactly(new Section(
                                Distance.from(10),
                                true,
                                new Station(saveUpStationId, "잠실"),
                                new Station(saveDownStationId, "잠실나루")
                        ))
        );
    }
}
