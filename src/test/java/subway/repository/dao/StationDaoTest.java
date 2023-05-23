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
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StationDao stationDao;
    private SectionDao sectionDao;
    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
        sectionDao = new SectionDao(jdbcTemplate);
        stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    void 여러_Station을_저장한다() {
        // given
        List<StationEntity> stations = List.of(new StationEntity("강남역"), new StationEntity("역삼역"));

        // when
        stationDao.insertAll(stations);

        // then
        assertThat(stationDao.findAll()).hasSize(2);
    }

    @Test
    void 저장된_역의_id들로_삭제한다() {
        // given
        StationEntity first = stationDao.insert(new StationEntity("강남역"));
        StationEntity second = stationDao.insert(new StationEntity("역삼역"));

        // when
        stationDao.deleteByIds(List.of(first.getId(), second.getId()));

        // then
        assertThat(stationDao.findAll()).hasSize(0);
    }

    @Test
    void 이름으로_역을_조회한다() {
        // given
        stationDao.insert(new StationEntity("강남역"));

        // when, then
        assertThat(stationDao.findByName("강남역")).isNotEmpty();
    }

    @Test
    void 구간에_저장되어_있는_역을_조회한다() {
        // given
        StationEntity first = stationDao.insert(new StationEntity("강남역"));
        StationEntity second = stationDao.insert(new StationEntity("역삼역"));
        LineEntity lineEntity = lineDao.insert(new LineEntity("2호선"));
        sectionDao.insert(new SectionEntity(first.getId(), second.getId(), lineEntity.getId(), 10));

        stationDao.insert(new StationEntity("잠실역"));

        //when
        List<StationEntity> stationsBySections = stationDao.findAllBySections();

        assertThat(stationsBySections).containsOnly(first, second);
    }

    @Test
    void 노선_ID로_구간에_저장되어_있는_역을_조회한다() {
        // given
        StationEntity first = stationDao.insert(new StationEntity("강남역"));
        StationEntity second = stationDao.insert(new StationEntity("역삼역"));
        LineEntity lineEntity = lineDao.insert(new LineEntity("2호선"));
        sectionDao.insert(new SectionEntity(first.getId(), second.getId(), lineEntity.getId(), 10));

        stationDao.insert(new StationEntity("잠실역"));

        //when
        List<StationEntity> stationsByLineId = stationDao.findByLineId(lineEntity.getId());

        assertThat(stationsByLineId).containsOnly(first, second);
    }
}
