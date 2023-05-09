package subway.domain.station;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class StationDistanceTest {

    @ParameterizedTest(name = "역 사이의 거리는 1보다 작을 수 없다")
    @ValueSource(ints = {-1, 0})
    void 역_사이의_거리는_1보다_작을_수_없다(int input) {
        assertThatThrownBy(() -> new StationDistance(input))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
