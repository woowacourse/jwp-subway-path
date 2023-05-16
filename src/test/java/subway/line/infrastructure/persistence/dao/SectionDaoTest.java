package subway.line.infrastructure.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.common.RepositoryTest;
import subway.line.domain.Station;
import subway.line.infrastructure.persistence.entity.LineEntity;
import subway.line.infrastructure.persistence.entity.SectionEntity;
import subway.line.infrastructure.persistence.entity.StationEntity;

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
        final StationEntity 역1 = StationEntity.from(new Station("역1"));
        final StationEntity 역2 = StationEntity.from(new Station("역2"));
        final StationEntity 역3 = StationEntity.from(new Station("역3"));
        final StationEntity 역4 = StationEntity.from(new Station("역4"));
        final StationEntity 역5 = StationEntity.from(new Station("역5"));
        stationDao.save(역1);
        stationDao.save(역2);
        stationDao.save(역3);
        stationDao.save(역4);
        stationDao.save(역5);
        final LineEntity lineEntity = new LineEntity(UUID.randomUUID(), "1호선");
        lineDao.save(lineEntity);
        final UUID lineId = lineEntity.domainId();
        final List<SectionEntity> sectionEntities = List.of(
                new SectionEntity(역1.domainId(), 역2.domainId(), 1, lineId),
                new SectionEntity(역2.domainId(), 역3.domainId(), 2, lineId),
                new SectionEntity(역3.domainId(), 역4.domainId(), 3, lineId),
                new SectionEntity(역4.domainId(), 역5.domainId(), 4, lineId)
        );

        // when
        sectionDao.batchSave(sectionEntities);

        // then
        assertThat(sectionDao.findAllByLineName("1호선")).hasSize(4);
    }

    @Test
    void 노선에_속한_구간을_모두_제거한다() {
        // given
        final StationEntity 역1 = StationEntity.from(new Station("역1"));
        final StationEntity 역2 = StationEntity.from(new Station("역2"));
        final StationEntity 역3 = StationEntity.from(new Station("역3"));
        final StationEntity 역4 = StationEntity.from(new Station("역4"));
        final StationEntity 역5 = StationEntity.from(new Station("역5"));
        stationDao.save(역1);
        stationDao.save(역2);
        stationDao.save(역3);
        stationDao.save(역4);
        stationDao.save(역5);

        final LineEntity lineEntity = new LineEntity(UUID.randomUUID(), "1호선");
        lineDao.save(lineEntity);
        final UUID lineId = lineEntity.domainId();

        final List<SectionEntity> sectionEntities = List.of(
                new SectionEntity(역1.domainId(), 역2.domainId(), 1, lineId),
                new SectionEntity(역2.domainId(), 역3.domainId(), 2, lineId),
                new SectionEntity(역3.domainId(), 역4.domainId(), 3, lineId),
                new SectionEntity(역4.domainId(), 역5.domainId(), 4, lineId)
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
        final StationEntity 역1 = StationEntity.from(new Station("역1"));
        final StationEntity 역2 = StationEntity.from(new Station("역2"));
        final StationEntity 역3 = StationEntity.from(new Station("역3"));
        final StationEntity 역4 = StationEntity.from(new Station("역4"));
        final StationEntity 역5 = StationEntity.from(new Station("역5"));
        stationDao.save(역1);
        stationDao.save(역2);
        stationDao.save(역3);
        stationDao.save(역4);
        stationDao.save(역5);

        final LineEntity lineEntity = new LineEntity(UUID.randomUUID(), "1호선");
        lineDao.save(lineEntity);
        final UUID lineId = lineEntity.domainId();

        final List<SectionEntity> sectionEntities = List.of(
                new SectionEntity(역1.domainId(), 역2.domainId(), 1, lineId),
                new SectionEntity(역2.domainId(), 역3.domainId(), 2, lineId),
                new SectionEntity(역3.domainId(), 역4.domainId(), 3, lineId),
                new SectionEntity(역4.domainId(), 역5.domainId(), 4, lineId)
        );
        sectionDao.batchSave(sectionEntities);

        // when
        final List<SectionEntity> allByLineName = sectionDao.findAllByLineName("1호선");

        // then
        assertThat(allByLineName)
                .extracting(SectionEntity::distance)
                .containsExactly(1, 2, 3, 4);
    }
}
