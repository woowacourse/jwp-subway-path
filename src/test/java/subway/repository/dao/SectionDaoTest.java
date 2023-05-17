package subway.repository.dao;

import static org.assertj.core.api.Assertions.assertThat;

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
    void Section을_저장한다() {
        // given
        LineEntity saveLine = lineDao.insert(new LineEntity("2호선"));
        StationEntity source = stationDao.insert(new StationEntity("강남역"));
        StationEntity target = stationDao.insert(new StationEntity("역삼역"));

        // when
        sectionDao.insert(new SectionEntity(source.getId(), target.getId(), saveLine.getId(), 6));
        List<SectionEntity> saveSections = sectionDao.findByLineId(saveLine.getId());

        // then
        assertThat(saveSections).hasSize(1);
    }

    @Test
    void 여러개의_Section을_저장한다() {
        // given
        LineEntity saveLine = lineDao.insert(new LineEntity("2호선"));
        final Long lineId = saveLine.getId();
        StationEntity station1 = stationDao.insert(new StationEntity("강남역"));
        StationEntity station2 = stationDao.insert(new StationEntity("역삼역"));
        StationEntity station3 = stationDao.insert(new StationEntity("선릉역"));
        StationEntity station4 = stationDao.insert(new StationEntity("삼성역"));
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
