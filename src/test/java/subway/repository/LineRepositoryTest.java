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
        lineRepository = new LineRepository(new SectionDao(jdbcTemplate), new LineDao(jdbcTemplate),
                stationDao);
    }

    @Test
    void 노선을_저장한다() {
        // given
        List<Section> sections = List.of(new Section("교대역", "강남역", 10), new Section("강남역", "역삼역", 5));
        Line line = new Line(null, "2호선", sections);

        // when
        Long saveId = lineRepository.save(line);

        // then
        assertThat(saveId).isPositive();
    }

    @Test
    void 전체_노선을_조회한다() {
        // given
        List<Section> firstSections = List.of(new Section("서울역", "명동역", 10), new Section("명동역", "광화문역", 7),
                new Section("민트역", "서울역", 5));
        Line firstLine = new Line(null, "1호선", firstSections);
        List<Section> secondSections = List.of(new Section("교대역", "강남역", 10), new Section("강남역", "역삼역", 5),
                new Section("박스터역", "교대역", 2));
        Line secondLine = new Line(null, "2호선", secondSections);
        lineRepository.save(firstLine);
        lineRepository.save(secondLine);

        // when
        List<Line> lines = lineRepository.findAll();

        // then
        assertThat(lines).hasSize(2);
        assertThat(lines).flatExtracting(Line::getStations)
                .contains(
                        new Station("서울역"),
                        new Station("명동역"),
                        new Station("광화문역"),
                        new Station("민트역"),
                        new Station("교대역"),
                        new Station("강남역"),
                        new Station("역삼역"),
                        new Station("박스터역")
                );
    }
}
