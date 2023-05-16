package subway.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

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

}