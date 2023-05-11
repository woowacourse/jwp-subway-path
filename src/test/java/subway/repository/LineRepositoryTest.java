package subway.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subway.domain.Line;
import subway.domain.Section;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class LineRepositoryTest {

    @Autowired
    private LineRepository lineRepository;

    @Test
    void 노선을_저장한다() {
        // given
        final Line line = new Line("2호선", "RED", List.of(
                new Section("B", "C", 3),
                new Section("A", "B", 2),
                new Section("D", "E", 5),
                new Section("C", "D", 4)
        ));

        // when
        final Line savedLine = lineRepository.save(line);

        // then
        assertThat(savedLine).usingRecursiveComparison().isEqualTo(line);
    }

    @Test
    void 노선을_조회한다() {
        // given
        final Line line = new Line("2호선", "RED", List.of(
                new Section("B", "C", 3),
                new Section("A", "B", 2),
                new Section("D", "E", 5),
                new Section("C", "D", 4)
        ));
        lineRepository.save(line);

        // when
        final List<Line> result = lineRepository.findAll();

        // then
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(line);
    }

    @Test
    void 노선을_삭제한다() {
        // given
        final Line line = new Line("2호선", "RED", List.of(
                new Section("B", "C", 3),
                new Section("A", "B", 2),
                new Section("D", "E", 5),
                new Section("C", "D", 4)
        ));
        final Line savedLine = lineRepository.save(line);

        // when
        lineRepository.delete(savedLine);

        // then
        assertThat(lineRepository.findAll()).isEmpty();
    }
}
