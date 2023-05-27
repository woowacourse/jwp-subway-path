package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.domain.line.Distance;
import subway.exception.section.NegativeDistanceValueException;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DistanceTest {

    @Test
    void 거리는_음수가_될_수_없다() {
        assertThatThrownBy(() -> new Distance(-1))
                .isInstanceOf(NegativeDistanceValueException.class)
                .hasMessage("거리는 음수가 될 수 없습니다 (입력값: -1)");
    }
}
