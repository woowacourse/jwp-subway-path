package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.entity.SectionEntity;
import subway.fixture.LineFixture.Line1;
import subway.fixture.StationFixture.A;
import subway.fixture.StationFixture.B;
import subway.fixture.StationFixture.C;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql({"classpath:schema-test.sql"})
class SectionDaoTest {

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
    void 여러_구간을_받아_저장한다() {
        // given
        final Long stationAid = stationDao.insert(A.entity);
        final Long stationBid = stationDao.insert(B.entity);
        final Long stationCid = stationDao.insert(C.entity);

        final Long lineId = lineDao.insert(Line1.entity);

        final List<SectionEntity> sectionEntities = List.of(
                new SectionEntity(lineId, stationAid, stationBid, 5),
                new SectionEntity(lineId, stationBid, stationCid, 6)
        );

        // when
        sectionDao.batchInsert(sectionEntities);

        // then
        assertThat(sectionDao.findAllByLineName("1호선")).hasSize(2);
    }

    @Test
    void 저장된_모든_구간을_삭제한다() {
        // given
        final Long stationAid = stationDao.insert(A.entity);
        final Long stationBid = stationDao.insert(B.entity);
        final Long stationCid = stationDao.insert(C.entity);

        final Long lineId = lineDao.insert(Line1.entity);

        final List<SectionEntity> sectionEntities = List.of(
                new SectionEntity(lineId, stationAid, stationBid, 5),
                new SectionEntity(lineId, stationBid, stationCid, 6)
        );

        sectionDao.batchInsert(sectionEntities);

        // when
        sectionDao.deleteAllByLineName("1호선");

        // then
        assertThat(sectionDao.findAllByLineName("1호선")).hasSize(0);
    }

    @Test
    @Order(1)
    void 여러구간을_받아_삭제한다() {
        // given
        final Long stationAid = stationDao.insert(A.entity);
        final Long stationBid = stationDao.insert(B.entity);
        final Long stationCid = stationDao.insert(C.entity);

        final Long lineId = lineDao.insert(Line1.entity);

        final List<SectionEntity> sectionEntities = List.of(
                new SectionEntity(1L, lineId, stationAid, stationBid, 5),
                new SectionEntity(2L, lineId, stationBid, stationCid, 6)
        );

        sectionDao.batchInsert(sectionEntities);

        // when
        sectionDao.batchDelete(sectionEntities);

        // then
        assertThat(sectionDao.findAllByLineName("1호선")).hasSize(0);
    }
}
