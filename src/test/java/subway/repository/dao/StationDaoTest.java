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
import subway.domain.Line;
import subway.domain.Section;
import subway.entity.StationEntity;
import subway.repository.LineRepository;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private LineRepository lineRepository;
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
        lineRepository = new LineRepository(
                new SectionDao(jdbcTemplate),
                new LineDao(jdbcTemplate),
                stationDao
        );
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
    void 노선에_속한_모든_역을_조회한다() {
        // given
        final Line line = new Line(null, "2호선", List.of(new Section("교대역", "강남역", 5), new Section("강남역", "역삼역", 7)));
        final Long saveId = lineRepository.save(line);

        // when
        final List<StationEntity> stations = stationDao.findByLineId(saveId);

        // then
        assertThat(stations).map(StationEntity::getName)
                .contains("교대역", "강남역", "역삼역");
    }
}
