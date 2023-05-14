package subway.domain.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.InvalidDistanceException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class DistanceTest {

    @ParameterizedTest
    @ValueSource(longs = {-1, 0})
    @DisplayName("거리가 1보다 작으면 예외를 발생시킨다.")
    void throws_exception_when_distance_is_invalid(final long givenDistance) {
        // when & then
        assertThatThrownBy(() -> new Distance(givenDistance))
                .isInstanceOf(InvalidDistanceException.class);
    }

    @Test
    @DisplayName("거리가 생성된다.")
    void create_distance_success() {
        // given
        long givenDistance = 3;

        // when
        Distance distance = new Distance(givenDistance);

        // then
        assertThat(distance.getDistance()).isEqualTo(givenDistance);
    }

    @Test
    @DisplayName("입력으로 온 거리가 현재 거리보다 큰지 확인한다.")
    void check_input_is_longer_than_now_distance() {
        // given
        long givenDistance = 3;
        long inputDistance = 5;
        Distance distance = new Distance(givenDistance);

        // when
        boolean result = distance.isShorterOrEqualThan(inputDistance);

        // then
        assertThat(result).isTrue();
    }
}
