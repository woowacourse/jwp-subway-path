package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.StationEntity;

import static org.assertj.core.api.Assertions.assertThat;


@Sql("/testdata.sql")
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SectionDao sectionDao;
    private LineDao lineDao;
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);
        stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    void 경로를_추가할_수_있다() {
        ///given
        final LineEntity line = new LineEntity("1호선", "blue");
        lineDao.insert(line);
        final StationEntity station1 = new StationEntity("충무로");
        final StationEntity station2 = new StationEntity("동대입구");
        stationDao.insert(station1);
        stationDao.insert(station2);

        ///when, then
        final SectionEntity section = new SectionEntity(1L, 2L, 1L, 10L);
        assertThat(sectionDao.insert(section).getId()).isSameAs(1L);
    }

    @Test
    void 노선_식별자에_해당하는_경로를_전체_조회한다() {
        ///given
        final LineEntity line = new LineEntity("1호선", "blue");
        lineDao.insert(line);
        final StationEntity station1 = new StationEntity("충무로");
        final StationEntity station2 = new StationEntity("동대입구");
        final StationEntity station3 = new StationEntity("약수");
        stationDao.insert(station1);
        stationDao.insert(station2);
        stationDao.insert(station3);
        final SectionEntity section1 = new SectionEntity(1L, 2L, 1L, 10L);
        final SectionEntity section2 = new SectionEntity(2L, 3L, 1L, 10L);
        sectionDao.insert(section1);
        sectionDao.insert(section2);

        ///when,then
        assertThat(sectionDao.findAllByLineId(1L).size()).isEqualTo(2);
    }

    @Test
    void 식별자를_통해_해당_경로를_조회한다() {
        ///given
        final LineEntity line = new LineEntity("1호선", "blue");
        lineDao.insert(line);
        final StationEntity station1 = new StationEntity("충무로");
        final StationEntity station2 = new StationEntity("동대입구");
        stationDao.insert(station1);
        stationDao.insert(station2);
        final SectionEntity section = new SectionEntity(1L, 2L, 1L, 10L);
        sectionDao.insert(section).getId();

        ///when,then
        assertThat(sectionDao.findById(1L).getId()).isEqualTo(1L);
    }
}