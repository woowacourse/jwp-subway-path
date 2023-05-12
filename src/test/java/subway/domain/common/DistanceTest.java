package subway.domain.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.DistanceForkedException;
import subway.exception.DistanceValueInvalidException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class DistanceTest {

    @ParameterizedTest
    @ValueSource(longs = {-1, 0})
    @DisplayName("거리가 1보다 작으면 예외를 발생시킨다.")
    void throws_exception_when_distance_is_invalid(final Long givenDistance) {
        // when & then
        assertThatThrownBy(() -> new Distance(givenDistance))
                .isInstanceOf(DistanceValueInvalidException.class);
    }

    @Test
    @DisplayName("거리가 생성된다.")
    void create_distance_success() {
        // given
        Long givenDistance = 3L;

        // when
        Distance distance = new Distance(givenDistance);

        // then
        assertThat(distance.getDistance()).isEqualTo(givenDistance);
    }

    @ParameterizedTest
    @ValueSource(longs = {5, 6, 10})
    @DisplayName("현재 길이보다 입력 길이가 같거나 크면 예외를 발생시킨다. (갈래길 방지)")
    void throws_exception_when_input_distance_is_forked_distance(final Long givenDistance) {
        Distance distance = new Distance(3L);

        // when & then
        assertThatThrownBy(() -> distance.validateSectionDistance(givenDistance))
                .isInstanceOf(DistanceForkedException.class);
    }
}
