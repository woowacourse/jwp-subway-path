package subway.repository.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.repository.dao.DaoFixtures.GANGNAM_STATION;
import static subway.repository.dao.DaoFixtures.LINE_NO_2;
import static subway.repository.dao.DaoFixtures.SAMSEONG_STATION;
import static subway.repository.dao.DaoFixtures.SEOLLEUNG_STATION;
import static subway.repository.dao.DaoFixtures.YEOKSAM_STATION;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SectionDao sectionDao;
    private LineDao lineDao;
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
        stationDao = new StationDao(jdbcTemplate);
        sectionDao = new SectionDao(jdbcTemplate);
    }

    @Test
    void 구간을_저장한다() {
        // given
        LineEntity saveLine = lineDao.insert(LINE_NO_2);
        StationEntity source = stationDao.insert(GANGNAM_STATION);
        StationEntity target = stationDao.insert(YEOKSAM_STATION);

        // when
        sectionDao.insert(new SectionEntity(source.getId(), target.getId(), saveLine.getId(), 6));
        List<SectionEntity> saveSections = sectionDao.findByLineId(saveLine.getId());

        // then
        assertThat(saveSections).hasSize(1);
    }

    @Test
    void 여러개의_구간을_저장한다() {
        // given
        LineEntity saveLine = lineDao.insert(LINE_NO_2);
        final Long lineId = saveLine.getId();
        StationEntity station1 = stationDao.insert(GANGNAM_STATION);
        StationEntity station2 = stationDao.insert(YEOKSAM_STATION);
        StationEntity station3 = stationDao.insert(SEOLLEUNG_STATION);
        StationEntity station4 = stationDao.insert(SAMSEONG_STATION);
        final SectionEntity section1 = new SectionEntity(station1.getId(), station2.getId(), lineId, 5);
        final SectionEntity section2 = new SectionEntity(station2.getId(), station3.getId(), lineId, 6);
        final SectionEntity section3 = new SectionEntity(station3.getId(), station4.getId(), lineId, 7);

        // when
        sectionDao.insertAll(List.of(section1, section2, section3));
        final List<SectionEntity> sections = sectionDao.findAll();

        // then
        assertThat(sections).hasSize(3);
    }
}
