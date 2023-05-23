package subway.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.fixture.StationFixture.GangnamStation;
import subway.fixture.StationFixture.JamsilStation;
import subway.line.domain.entity.LineEntity;
import subway.line.presist.LineDao;
import subway.section.domain.entity.SectionEntity;
import subway.section.exception.SectionNotFoundException;
import subway.section.persist.SectionDao;
import subway.station.domain.entity.StationEntity;
import subway.station.persist.StationDao;
import subway.vo.Name;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
@Sql({"classpath:schema-test.sql"})
class SectionEntityDaoTest {

    private LineDao lineDao;
    private StationDao stationDao;
    private SectionDao sectionDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        this.lineDao = new LineDao(jdbcTemplate);
        this.stationDao = new StationDao(jdbcTemplate);
        this.sectionDao = new SectionDao(jdbcTemplate);
    }

    @Test
    void 섹션을_받아_저장한다() {
        // given
        final Long lineId = lineDao.insert(LineEntity.of("2호선", "초록"));
        final Long stationId1 = stationDao.insert(GangnamStation.GANGNAM_STATION_ENTITY);
        final Long stationId2 = stationDao.insert(JamsilStation.JAMSIL_STATION_ENTITY);

        final SectionEntity sectionEntity = SectionEntity.of(lineId, stationId1, stationId2, 3);

        // when
        final Long id = sectionDao.insert(sectionEntity);

        //then
        assertThat(id).isPositive();
    }

    @Test
    void 상행역의_아이디를_받아_구간을_조회한다() {
        // given
        final Long lineId = lineDao.insert(LineEntity.of("2호선", "초록"));
        final Long stationId1 = stationDao.insert(GangnamStation.GANGNAM_STATION_ENTITY);
        final Long stationId2 = stationDao.insert(JamsilStation.JAMSIL_STATION_ENTITY);

        final SectionEntity sectionEntity = SectionEntity.of(lineId, stationId1, stationId2, 3);
        sectionDao.insert(sectionEntity);

        // when
        SectionEntity section = sectionDao.findByUpStationId(stationId1)
                .orElseThrow(() -> SectionNotFoundException.THROW);

        // then
        Assertions.assertAll(
                () -> assertThat(section.getDistance()).isEqualTo(3),
                () -> assertThat(section.getUpStationId()).isEqualTo(stationId1),
                () -> assertThat(section.getDownStationId()).isEqualTo(stationId2),
                () -> assertThat(section.getLineId()).isEqualTo(lineId)
        );
    }

    @Test
    void 모든_구간을_조회한다() {
        // given
        final Long lineId = lineDao.insert(LineEntity.of("2호선", "초록"));
        final Long stationId1 = stationDao.insert(GangnamStation.GANGNAM_STATION_ENTITY);
        final Long stationId2 = stationDao.insert(JamsilStation.JAMSIL_STATION_ENTITY);

        final SectionEntity sectionEntity1 = SectionEntity.of(lineId, stationId1, stationId2, 3);
        sectionDao.insert(sectionEntity1);

        final Long stationId3 = stationDao.insert(StationEntity.of(Name.from("선릉역")));
        SectionEntity stationEntity2 = SectionEntity.of(lineId, stationId2, stationId3, 10);
        sectionDao.insert(stationEntity2);

        // when
        List<SectionEntity> sections = sectionDao.findAll();

        // then
        Assertions.assertAll(
                () -> assertThat(sections).extracting(SectionEntity::getDistance)
                        .contains(3, 10),
                () -> assertThat(sections).extracting(SectionEntity::getLineId).contains(lineId),
                () -> assertThat(sections).extracting(SectionEntity::getUpStationId)
                        .contains(stationId1, stationId2),
                () -> assertThat(sections).extracting(SectionEntity::getDownStationId)
                        .contains(stationId2, stationId3)
        );
    }

    @Test
    void 역의_아이디를_입력받아_왼쪽_구간을_조회한다() {
        // given
        final Long lineId = lineDao.insert(LineEntity.of("2호선", "초록"));
        final Long stationId1 = stationDao.insert(GangnamStation.GANGNAM_STATION_ENTITY);
        final Long stationId2 = stationDao.insert(JamsilStation.JAMSIL_STATION_ENTITY);

        final SectionEntity sectionEntity = SectionEntity.of(lineId, stationId1, stationId2, 3);
        sectionDao.insert(sectionEntity);

        // when
        SectionEntity section = sectionDao.findLeftSectionByStationId(stationId2)
                .orElseThrow(() -> SectionNotFoundException.THROW);

        // then
        Assertions.assertAll(
                () -> assertThat(section.getUpStationId()).isEqualTo(stationId1),
                () -> assertThat(section.getDownStationId()).isEqualTo(stationId2),
                () -> assertThat(section.getDistance()).isEqualTo(3)
        );
    }

    @Test
    void 역의_아이디를_입력받아_오른쪽_구간을_조회한다() {
        // given
        final Long lineId = lineDao.insert(LineEntity.of("2호선", "초록"));
        final Long stationId1 = stationDao.insert(GangnamStation.GANGNAM_STATION_ENTITY);
        final Long stationId2 = stationDao.insert(JamsilStation.JAMSIL_STATION_ENTITY);

        final SectionEntity sectionEntity = SectionEntity.of(lineId, stationId1, stationId2, 3);
        sectionDao.insert(sectionEntity);

        // when
        SectionEntity section = sectionDao.findRightSectionByStationId(stationId1)
                .orElseThrow(() -> SectionNotFoundException.THROW);

        // then
        Assertions.assertAll(
                () -> assertThat(section.getUpStationId()).isEqualTo(stationId1),
                () -> assertThat(section.getDownStationId()).isEqualTo(stationId2),
                () -> assertThat(section.getDistance()).isEqualTo(3)
        );
    }

    @Test
    void 구간의_아이디를_입력받아_구간을_삭제한다() {
        // given
        final Long lineId = lineDao.insert(LineEntity.of("2호선", "초록"));
        final Long stationId1 = stationDao.insert(GangnamStation.GANGNAM_STATION_ENTITY);
        final Long stationId2 = stationDao.insert(JamsilStation.JAMSIL_STATION_ENTITY);

        final SectionEntity sectionEntity = SectionEntity.of(lineId, stationId1, stationId2, 3);
        Long sectionId = sectionDao.insert(sectionEntity);

        // when
        sectionDao.deleteById(sectionId);

        // then
        assertThat(sectionDao.findAll().size()).isEqualTo(0);
    }

}
