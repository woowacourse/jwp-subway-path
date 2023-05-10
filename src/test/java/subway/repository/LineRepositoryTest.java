package subway.repository;

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
import subway.domain.Station;
import subway.repository.dao.LineDao;
import subway.repository.dao.SectionDao;
import subway.repository.dao.StationDao;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class LineRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StationDao stationDao;
    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
        lineRepository = new LineRepository(new SectionDao(jdbcTemplate), new LineDao(jdbcTemplate));
    }

    @Test
    void 노선을_저장한다() {
        Station source = stationDao.insert(new Station("역삼역"));
        Station target = stationDao.insert(new Station("강남역"));
        Line line = new Line("2호선", List.of(new Section(source, target, 10)));

        Line saveLine = lineRepository.save(line);

        assertThat(saveLine.getId()).isNotNull();
        assertThat(line).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(saveLine);
    }
}
