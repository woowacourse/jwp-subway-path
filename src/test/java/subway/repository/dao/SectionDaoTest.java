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
    void 여러_Section을_저장한다() {
        // given
        LineEntity saveLine = lineDao.insert(new LineEntity("2호선"));
        StationEntity source = stationDao.insert(new StationEntity("강남역"));
        StationEntity target = stationDao.insert(new StationEntity("역삼역"));

        // when
        Long saveId = sectionDao.insert(new SectionEntity(source.getId(), target.getId(), saveLine.getId(), 6));

        // then
        List<SectionEntity> saveSections = sectionDao.findByLineId(saveLine.getId());
        assertThat(saveSections).hasSize(1);
    }

}
