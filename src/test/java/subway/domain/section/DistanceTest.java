package subway.domain.section;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.distance.InvalidDistanceException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DistanceTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, -100, -1000})
    @DisplayName("음수 거리는 생성될 수 없다.")
    void validate_distance_positive(int distance) {
        // when + then
        assertThatThrownBy(() -> new Distance(distance))
                .isInstanceOf(InvalidDistanceException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 100, 1000})
    @DisplayName("거리는 자연수여야 한다.")
    void generate_distance_success(int distance) {
        // when + then
        assertDoesNotThrow(() -> new Distance(distance));
    }
}
