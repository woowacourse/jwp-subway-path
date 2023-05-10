package subway.domain.station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class StationDistanceTest {

    @ParameterizedTest(name = "역 사이의 거리는 1보다 작을 수 없다")
    @ValueSource(ints = {-1, 0})
    void 역_사이의_거리는_1보다_작을_수_없다(final int input) {
        assertThatThrownBy(() -> new StationDistance(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 거리간_뺄셈_테스트() {
        //given
        final StationDistance five = new StationDistance(5);
        final StationDistance three = new StationDistance(3);

        //when
        final StationDistance two = five.subtract(three);

        //then
        assertThat(two).isEqualTo(new StationDistance(2));
    }

    @Test
    void 거리간_덧셈_테스트() {
        //given
        final StationDistance five = new StationDistance(5);
        final StationDistance three = new StationDistance(3);

        //when
        final StationDistance eight = five.sum(three);

        //then
        assertThat(eight).isEqualTo(new StationDistance(8));
    }
}
