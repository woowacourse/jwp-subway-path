package subway.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Line;
import subway.domain.SectionEntity;
import subway.domain.Station;
import java.util.List;

@JdbcTest
@Sql("/schema.sql")
class SectionDaoTest {

    private SectionDao sectionDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final Station 신림역 = new Station(1L, "신림");
    private final Station 봉천역 = new Station(2L, "봉천");
    private final Line _2호선 = new Line(1L, "2호선", "초록색", 0);

    @BeforeEach
    void setUp() {
        this.sectionDao = new SectionDao(jdbcTemplate);
        StationDao stationDao = new StationDao(jdbcTemplate);
        LineDao lineDao = new LineDao(jdbcTemplate);

        stationDao.insert(신림역);
        stationDao.insert(봉천역);
        lineDao.insert(_2호선);
    }

    @Test
    void Section_삽입() {
        // given
        SectionEntity sectionEntity = new SectionEntity(1L, 1L, 2L, 10);

        // when
        SectionEntity savedSectionEntity = sectionDao.insert(sectionEntity);

        // then
        assertThat(savedSectionEntity.getId()).isEqualTo(1L);
    }

    @Test
    void 모든_Section_조회() {
        assertThat(sectionDao.findAll()).hasSize(0);

        // given
        SectionEntity sectionEntity = new SectionEntity(1L, 1L, 2L, 10);
        sectionDao.insert(sectionEntity);

        // expect
        assertThat(sectionDao.findAll()).hasSize(1);
    }

    @Test
    void ID로_단일_Section_조회() {
        // given
        SectionEntity sectionEntity = new SectionEntity(1L, 1L, 2L, 10);
        SectionEntity savedSectionEntity = sectionDao.insert(sectionEntity);
        long savedId = savedSectionEntity.getId();

        // when
        SectionEntity foundSectionEntity = sectionDao.findById(savedId);

        // then
        assertThat(foundSectionEntity).isEqualTo(savedSectionEntity);
    }

    @Test
    void ID로_Section_삭제() {
        // given
        SectionEntity sectionEntity = new SectionEntity(1L, 1L, 2L, 10);
        SectionEntity savedSectionEntity = sectionDao.insert(sectionEntity);
        long savedId = savedSectionEntity.getId();

        // when
        sectionDao.deleteById(savedId);

        // then
        assertThatThrownBy(() -> sectionDao.findById(savedId));
    }

    @Test
    void 특정_호선ID와_일치하는_모든_Section_조회() {
        // given
        SectionEntity sectionEntity = new SectionEntity(1L, 1L, 2L, 10);
        sectionDao.insert(sectionEntity);

        // when
        List<SectionEntity> result = sectionDao.findByLineId(1L);
        List<SectionEntity> result2 = sectionDao.findByLineId(2L);

        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result2).hasSize(0)
        );
    }

    @Test
    void 특정_호선ID와_일치하는_모든_Section_삭제() {
        long lineId = 1L;

        SectionEntity sectionEntity = new SectionEntity(lineId, 1L, 2L, 10);
        sectionDao.insert(sectionEntity);
        assertThat(sectionDao.findAll()).hasSize(1);

        sectionDao.deleteAllByLineId(lineId);
        assertThat(sectionDao.findByLineId(lineId)).hasSize(0);
    }
}
