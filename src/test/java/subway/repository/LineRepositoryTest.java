package subway.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.core.Line;
import subway.domain.core.Section;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Transactional
@SpringBootTest
class LineRepositoryTest {

    @Autowired
    private LineRepository lineRepository;

    @Test
    void 노선을_저장한다() {
        // given
        final Line line = new Line("2호선", "RED", 0, List.of(
                new Section("B", "C", 3),
                new Section("A", "B", 2),
                new Section("D", "E", 5),
                new Section("C", "D", 4)
        ));

        // when
        final Line savedLine = lineRepository.save(line);

        // then
        assertThat(savedLine).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(line);
    }

    @Test
    void 노선을_조회한다() {
        // given
        final Line line = new Line("2호선", "RED", 0, List.of(
                new Section("B", "C", 3),
                new Section("A", "B", 2),
                new Section("D", "E", 5),
                new Section("C", "D", 4)
        ));
        lineRepository.save(line);

        // when
        final List<Line> result = lineRepository.findAll();

        // then
        assertThat(result.get(0)).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(line);
    }

    @Test
    void 노선을_삭제한다() {
        // given
        final Line line = new Line("2호선", "RED", 0, List.of(
                new Section("B", "C", 3),
                new Section("A", "B", 2),
                new Section("D", "E", 5),
                new Section("C", "D", 4)
        ));
        final Line savedLine = lineRepository.save(line);
        final Long id = lineRepository.findIdByName(savedLine.getName()).orElseThrow();

        // when
        lineRepository.deleteById(id);

        // then
        assertThat(lineRepository.findAll()).isEmpty();
    }

    @Test
    void 이름을_입력받아_라인의_id를_반환한다() {
        // given
        final Line line = new Line("2호선", "RED", 0, List.of(
                new Section("B", "C", 3),
                new Section("A", "B", 2),
                new Section("D", "E", 5),
                new Section("C", "D", 4)
        ));
        lineRepository.save(line);

        // when
        final Long id = lineRepository.findIdByName("2호선").orElseThrow();

        // then
        assertThat(id).isPositive();
    }

    @Test
    void id를_입력받아_라인을_반환한다() {
        // given
        final Line line = new Line("2호선", "RED", 0, List.of(
                new Section("B", "C", 3),
                new Section("A", "B", 2),
                new Section("D", "E", 5),
                new Section("C", "D", 4)
        ));
        lineRepository.save(line);
        final Long id = lineRepository.findIdByName(line.getName()).orElseThrow();

        // when
        final Line result = lineRepository.findById(id).orElseThrow();

        // then
        assertThat(result).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(line);
    }
}
