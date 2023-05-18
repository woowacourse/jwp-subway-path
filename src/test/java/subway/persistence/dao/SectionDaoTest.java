package subway.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.persistence.RepositoryTest;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.StationEntity;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("SectionDao 은(는)")
@RepositoryTest
class SectionDaoTest {

    @Autowired
    private StationDao stationDao;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private SectionDao sectionDao;

    @Test
    void 여러_구간을_저장한다() {
        // given
        final Long 역1 = stationDao.save(new StationEntity("역1"));
        final Long 역2 = stationDao.save(new StationEntity("역2"));
        final Long 역3 = stationDao.save(new StationEntity("역3"));
        final Long 역4 = stationDao.save(new StationEntity("역4"));
        final Long 역5 = stationDao.save(new StationEntity("역5"));

        final Long lineId = lineDao.save(new LineEntity("1호선", 200));

        final List<SectionEntity> sectionEntities = List.of(
                new SectionEntity(역1, 역2, 1, lineId),
                new SectionEntity(역2, 역3, 2, lineId),
                new SectionEntity(역3, 역4, 3, lineId),
                new SectionEntity(역4, 역5, 4, lineId)
        );

        // when
        sectionDao.batchSave(sectionEntities);

        // then
        assertThat(sectionDao.findAllByLineName("1호선")).hasSize(4);
    }

    @Test
    void 노선에_속한_구간을_모두_제거한다() {
        // given
        final Long 역1 = stationDao.save(new StationEntity("역1"));
        final Long 역2 = stationDao.save(new StationEntity("역2"));
        final Long 역3 = stationDao.save(new StationEntity("역3"));
        final Long 역4 = stationDao.save(new StationEntity("역4"));
        final Long 역5 = stationDao.save(new StationEntity("역5"));

        final Long lineId = lineDao.save(new LineEntity("1호선", 400));

        final List<SectionEntity> sectionEntities = List.of(
                new SectionEntity(역1, 역2, 1, lineId),
                new SectionEntity(역2, 역3, 2, lineId),
                new SectionEntity(역3, 역4, 3, lineId),
                new SectionEntity(역4, 역5, 4, lineId)
        );
        sectionDao.batchSave(sectionEntities);

        // when
        sectionDao.deleteAllByLineName("1호선");

        // then
        assertThat(sectionDao.findAllByLineName("1호선")).hasSize(0);
    }

    @Test
    void 노선에_속한_구간을_모두_조회한다() {
        // given
        final Long 역1 = stationDao.save(new StationEntity("역1"));
        final Long 역2 = stationDao.save(new StationEntity("역2"));
        final Long 역3 = stationDao.save(new StationEntity("역3"));
        final Long 역4 = stationDao.save(new StationEntity("역4"));
        final Long 역5 = stationDao.save(new StationEntity("역5"));

        final Long lineId = lineDao.save(new LineEntity("1호선", 200));

        final List<SectionEntity> sectionEntities = List.of(
                new SectionEntity(역1, 역2, 1, lineId),
                new SectionEntity(역2, 역3, 2, lineId),
                new SectionEntity(역3, 역4, 3, lineId),
                new SectionEntity(역4, 역5, 4, lineId)
        );
        sectionDao.batchSave(sectionEntities);

        // when
        final List<SectionEntity> allByLineName = sectionDao.findAllByLineName("1호선");

        // then
        assertThat(allByLineName)
                .extracting(SectionEntity::getDistance)
                .containsExactly(1, 2, 3, 4);
    }
}
