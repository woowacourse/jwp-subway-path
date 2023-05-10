package subway.domain.section;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SectionDistanceTest {

    @ParameterizedTest(name = "거리가 1 이상 50 이하면 정상 생성된다.")
    @ValueSource(ints = {1, 50})
    void station_name_success_test(final int distance) {
        final SectionDistance sectionDistance = assertDoesNotThrow(() -> new SectionDistance(distance));
        assertThat(sectionDistance)
            .extracting("distance")
            .isEqualTo(distance);
    }

    @ParameterizedTest(name = "거리가 1 미만 50 초과면 예외가 발생한다.")
    @ValueSource(ints = {0, 51})
    void station_name_fail_test(final int distance) {
        assertThatThrownBy(() -> new SectionDistance(distance))
            .isInstanceOf(IllegalArgumentException.class)
            .extracting("message")
            .isEqualTo("거리는 최소 1부터 최대 50까지 가능합니다.");
    }
}
