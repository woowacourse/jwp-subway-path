package subway.persistence.repository;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import subway.domain.Line;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SuppressWarnings("NonAsciiCharacters")
class LineRepositoryTest extends RepositoryTest {

    @Test
    void 노선을_저장한다() {
        final Line line = Line.of("경의중앙선", "bg-blue-500");

        final Line actual = lineRepository.insert(line);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual.getName()).isEqualTo("경의중앙선");
            softAssertions.assertThat(actual.getColor()).isEqualTo("bg-blue-500");
        });
    }

    @Test
    void 모든_노선을_조회한다() {
        final Line line = Line.of("경의중앙선", "bg-red-500");
        lineRepository.insert(line);

        final List<Line> actual = lineRepository.findAll();

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.get(0).getId()).isPositive();
            softAssertions.assertThat(actual.get(0).getName()).isEqualTo("경의중앙선");
            softAssertions.assertThat(actual.get(0).getColor()).isEqualTo("bg-red-500");
        });
    }

    @Test
    void 노선_하나를_조회한다() {
        final Line line = Line.of("경의중앙선", "bg-red-500");
        final Line insertedLineEntity = lineRepository.insert(line);

        final Line actual = lineRepository.findById(insertedLineEntity.getId());

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual.getName()).isEqualTo("경의중앙선");
            softAssertions.assertThat(actual.getColor()).isEqualTo("bg-red-500");
        });
    }

    @Test
    void 노선_하나를_삭제한다() {
        final Line line = Line.of("경의중앙선", "bg-red-500");
        final Line insertedLineEntity = lineRepository.insert(line);

        assertDoesNotThrow(() -> lineRepository.deleteById(insertedLineEntity.getId()));
    }
}
