package subway.dao;

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
import subway.line.domain.repository.LineDao;
import subway.section.domain.entity.SectionEntity;
import subway.section.domain.repository.SectionDao;
import subway.station.domain.entity.StationEntity;
import subway.station.domain.repository.StationDao;
import subway.station.exception.StationNotFoundException;
import subway.vo.Name;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
@Sql({"classpath:schema-test.sql"})
class StationEntityDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private StationDao stationDao;
    private LineDao lineDao;
    private SectionDao sectionDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);
        sectionDao = new SectionDao(jdbcTemplate);
    }

    @Test
    void 역_엔티티를_받아_역을_저장한다() {
        // given
        final StationEntity stationEntity = JamsilStation.JAMSIL_STATION_ENTITY;

        // when
        final Long result = stationDao.insert(stationEntity);

        //then
        assertThat(result).isPositive();
    }

    @Test
    void 등록된_모든_역을_조회한다() {
        // given
        final Long id1 = stationDao.insert(JamsilStation.JAMSIL_STATION_ENTITY);
        final Long id2 = stationDao.insert(GangnamStation.GANGNAM_STATION_ENTITY);

        // when
        final List<StationEntity> result = stationDao.findAll();

        // then
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(1).getId()).isEqualTo(id1),
                () -> assertThat(result.get(1).getName()).isEqualTo("잠실역"),
                () -> assertThat(result.get(0).getId()).isEqualTo(id2),
                () -> assertThat(result.get(0).getName()).isEqualTo("강남역")
        );

    }

    @Test
    void 등록된_역을_Id로_조회한다() {
        // given
        final Long id = stationDao.insert(JamsilStation.JAMSIL_STATION_ENTITY);

        // when
        final StationEntity result = stationDao.findById(id)
                .orElseThrow(() -> StationNotFoundException.THROW);

        // then
        assertThat(result.getName()).isEqualTo("잠실역");
    }

    @Test
    void 등록된_역을_수정한다() {
        // given
        final Long id = stationDao.insert(GangnamStation.GANGNAM_STATION_ENTITY);
        final StationEntity 선릉역 = StationEntity.of(Name.from("선릉역"));

        // when
        stationDao.updateById(id, 선릉역);
        final StationEntity result = stationDao.findById(id)
                .orElseThrow(() -> StationNotFoundException.THROW);

        // then
        assertThat(result.getName()).isEqualTo("선릉역");
    }

    @Test
    void 등록된_역을_삭제한다() {
        // given
        final Long id = stationDao.insert(GangnamStation.GANGNAM_STATION_ENTITY);

        // when
        stationDao.deleteById(id);

        // then
        assertDoesNotThrow(() -> stationDao.findById(id));
    }

    @Test
    void 종점들을_반환한다() {
        // given
        final Long lineId1 = lineDao.insert(LineEntity.of("2호선", "초록"));

        final Long stationId1 = stationDao.insert(GangnamStation.GANGNAM_STATION_ENTITY);
        final Long stationId2 = stationDao.insert(JamsilStation.JAMSIL_STATION_ENTITY);
        final Long stationId3 = stationDao.insert(StationEntity.of(Name.from("잠실새내역")));

        final SectionEntity sectionEntity1 = SectionEntity.of(lineId1, stationId1, stationId2, 3);
        final SectionEntity sectionEntity2 = SectionEntity.of(lineId1, stationId2, stationId3, 3);
        sectionDao.insert(sectionEntity1);
        sectionDao.insert(sectionEntity2);

        // when
        final StationEntity upTerminalStationEntity = stationDao.findFinalUpStation(lineId1)
                .orElseThrow(() -> StationNotFoundException.THROW);
        final StationEntity downTerminalStationEntity = stationDao.findFinalDownStation(lineId1)
                .orElseThrow(() -> StationNotFoundException.THROW);

        // then
        assertThat(upTerminalStationEntity.getName()).isEqualTo("강남역");
        assertThat(downTerminalStationEntity.getName()).isEqualTo("잠실새내역");
    }

}
