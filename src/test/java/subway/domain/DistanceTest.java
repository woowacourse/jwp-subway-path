package subway.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistanceTest {

    @Test
    @DisplayName("값을 음수로 하는 거리 객체를 생성한다.")
    void throwExceptionWhenDistanceIsNegative() {
        //given
        int value = -100;
        //when
        //then
        assertThatThrownBy(() -> new Distance(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("거리를 비교 - 멀다")
    void isFartherThan() {
        //given
        Distance distance = new Distance(100);
        Distance otherDistance = new Distance(200);

        //when
        boolean actual = distance.isLongerThan(otherDistance);

        //then
        Assertions.assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("거리 비교 - 가까움")
    void isCloserThan() {
        //given
        Distance distance = new Distance(100);
        Distance otherDistance = new Distance(200);
        //when
        boolean actual = distance.isShorterThan(otherDistance);

        //then
        Assertions.assertThat(actual).isTrue();
    }

}