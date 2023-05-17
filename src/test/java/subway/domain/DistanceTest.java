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
    void diff() {
        var distance = new Distance(10);
        var other = new Distance(6);

        assertThat(distance.diff(other)).isEqualTo(new Distance(4));
        assertThat(other.diff(distance)).isEqualTo(new Distance(4));
    }
}
