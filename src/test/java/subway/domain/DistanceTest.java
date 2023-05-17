package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subway.domain.exception.NotPositiveDistanceException;

class DistanceTest {

    @DisplayName("거리는 양수여야 한다")
    @Test
    void requiresPositive() {
        assertThatThrownBy(() -> new Distance(0))
                .isInstanceOf(NotPositiveDistanceException.class);
    }

    @DisplayName("거리끼리 차를 구한다")
    @Test
    void gapBetweenOneAndOther() {
        var distance = new Distance(10);
        var other = new Distance(6);

        assertThat(distance.gapBetween(other)).isEqualTo(new Distance(4));
        assertThat(other.gapBetween(distance)).isEqualTo(new Distance(4));
    }

    @DisplayName("거리끼리 더한다")
    @Test
    void add() {
        var distance = new Distance(10);
        var other = new Distance(6);

        assertThat(distance.sum(other)).isEqualTo(new Distance(16));
    }

    @DisplayName("어느 거리가 더 긴지 알 수 있다")
    @Test
    void isDistanceLongerThanOther() {
        var distance = new Distance(10);
        var other = new Distance(9);

        assertThat(distance.isLongerThan(other)).isTrue();
    }
}
