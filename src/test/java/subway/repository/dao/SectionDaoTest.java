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
import subway.repository.entity.LineEntity;
import subway.repository.entity.SectionEntity;
import subway.repository.entity.StationEntity;

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
        sectionDao.insert(new SectionEntity(source.getId(), target.getId(), saveLine.getId(), 6));

        // then
        List<SectionEntity> saveSections = sectionDao.findAll();
        assertThat(saveSections).hasSize(1);
    }

    @Test
    void 노선_ID로_구간을_찾는다() {
        // given
        LineEntity saveLine = lineDao.insert(new LineEntity("2호선"));
        StationEntity source = stationDao.insert(new StationEntity("강남역"));
        StationEntity target = stationDao.insert(new StationEntity("역삼역"));
        sectionDao.insert(new SectionEntity(source.getId(), target.getId(), saveLine.getId(), 6));

        // when
        List<SectionEntity> saveSections = sectionDao.findByLineId(saveLine.getId());

        // then
        assertThat(saveSections).hasSize(1);
    }

    @Test
    void 노선_ID로_구간을_삭제한다() {
        // given
        LineEntity saveLine = lineDao.insert(new LineEntity("2호선"));
        StationEntity source = stationDao.insert(new StationEntity("강남역"));
        StationEntity target = stationDao.insert(new StationEntity("역삼역"));
        sectionDao.insert(new SectionEntity(source.getId(), target.getId(), saveLine.getId(), 6));

        LineEntity secondLine = lineDao.insert(new LineEntity("1호선"));
        StationEntity secondSource = stationDao.insert(new StationEntity("부산역"));
        StationEntity secondTarget = stationDao.insert(new StationEntity("서면역"));
        sectionDao.insert(new SectionEntity(secondSource.getId(), secondTarget.getId(), secondLine.getId(), 6));

        // when
        sectionDao.deleteByLineId(saveLine.getId());

        // then
        assertThat(sectionDao.findAll()).hasSize(1);
    }
}
