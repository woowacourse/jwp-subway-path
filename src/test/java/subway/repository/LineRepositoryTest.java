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
    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        lineRepository = new LineRepository(
                new SectionDao(jdbcTemplate),
                new LineDao(jdbcTemplate),
                new StationDao(jdbcTemplate)
        );
    }

    @Test
    void 노선을_저장한다() {
        // given
        List<Section> sections = List.of(new Section("교대역", "강남역", 10), new Section("강남역", "역삼역", 5));
        Line line = new Line("2호선", sections);

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
        Line firstLine = new Line("1호선", firstSections);
        List<Section> secondSections = List.of(new Section("교대역", "강남역", 10), new Section("강남역", "역삼역", 5),
                new Section("박스터역", "교대역", 2));
        Line secondLine = new Line("2호선", secondSections);
        lineRepository.save(firstLine);
        lineRepository.save(secondLine);

        // when
        List<Line> lines = lineRepository.findAll();

        // then
        assertThat(lines).hasSize(2);
        assertThat(lines).flatExtracting(Line::getStations)
                .containsOnly(
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

    @Test
    void 노선을_삭제한다() {
        // given
        List<Section> firstSections = List.of(new Section("A", "B", 10), new Section("B", "C", 7));
        Line firstLine = new Line("1호선", firstSections);
        List<Section> secondSections = List.of(new Section("X", "A", 10), new Section("A", "Y", 5));
        Line secondLine = new Line("2호선", secondSections);
        Long firstLineId = lineRepository.save(firstLine);
        lineRepository.save(secondLine);

        // when
        lineRepository.deleteById(firstLineId);

        // then
        assertThat(lineRepository.findAll()).flatExtracting(Line::getStations)
                .containsOnly(
                        new Station("X"),
                        new Station("Y"),
                        new Station("A")
                );
    }

    @Test
    void 노선에_역을_추가한다() {
        List<Section> firstSections = List.of(new Section("A", "B", 10), new Section("B", "C", 7));
        Line firstLine = new Line("1호선", firstSections);
        lineRepository.save(firstLine);
        firstLine.addSection(new Section(new Station("B"), new Station("D"), 5));

        lineRepository.save(firstLine);

        assertThat(lineRepository.findAll()).flatExtracting(Line::getSections)
                .containsOnly(
                        new Section("A", "B", 10),
                        new Section("B", "D", 5),
                        new Section("D", "C", 2)
                );
    }

    @Test
    void 노선에_역을_삭제한다() {
        List<Section> firstSections = List.of(new Section("A", "B", 10), new Section("B", "C", 7));
        Line firstLine = new Line("1호선", firstSections);
        lineRepository.save(firstLine);
        firstLine.removeStation(new Station("B"));

        lineRepository.save(firstLine);

        assertThat(lineRepository.findAll()).flatExtracting(Line::getSections)
                .containsOnly(
                        new Section("A", "C", 17)
                );
    }

    @Test
    void 노선_ID로_조회한다() {
        List<Section> firstSections = List.of(new Section("A", "B", 10), new Section("B", "C", 7));
        Line firstLine = new Line("1호선", firstSections);
        Long savedId = lineRepository.save(firstLine);

        Line findLine = lineRepository.findById(savedId);

        assertThat(findLine.getStations())
                .containsOnly(
                        new Station("A"),
                        new Station("B"),
                        new Station("C")
                );
    }
}
