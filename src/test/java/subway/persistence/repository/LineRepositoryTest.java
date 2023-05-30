package subway.persistence.repository;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import subway.domain.Line;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static subway.fixtures.domain.LineFixture.SECOND_LINE;

@SuppressWarnings("NonAsciiCharacters")
class LineRepositoryTest extends RepositoryTest {

    @Test
    void 노선을_저장한다() {
        final Line actual = lineRepository.insert(SECOND_LINE);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual.getName()).isEqualTo("2호선");
            softAssertions.assertThat(actual.getColor()).isEqualTo("bg-green-500");
        });
    }

    @Test
    void 모든_노선을_조회한다() {
        lineRepository.insert(SECOND_LINE);

        final List<Line> actual = lineRepository.findAll();

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.get(0).getId()).isPositive();
            softAssertions.assertThat(actual.get(0).getName()).isEqualTo("2호선");
            softAssertions.assertThat(actual.get(0).getColor()).isEqualTo("bg-green-500");
        });
    }

    @Test
    void 노선_하나를_조회한다() {
        final Line insertedLineEntity = lineRepository.insert(SECOND_LINE);

        final Line actual = lineRepository.findById(insertedLineEntity.getId());

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual.getName()).isEqualTo("2호선");
            softAssertions.assertThat(actual.getColor()).isEqualTo("bg-green-500");
        });
    }

    @Test
    void 노선_하나를_삭제한다() {
        final Line insertedLineEntity = lineRepository.insert(SECOND_LINE);

        assertDoesNotThrow(() -> lineRepository.deleteById(insertedLineEntity.getId()));
    }
}
