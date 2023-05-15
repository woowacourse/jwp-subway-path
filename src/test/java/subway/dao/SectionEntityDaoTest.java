package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.LineEntity;
import subway.domain.SectionEntity;
import subway.fixture.StationFixture.GangnamStation;
import subway.fixture.StationFixture.JamsilStation;

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

}
