package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.section.DistanceNotPositiveException;

class FareTest {

    @DisplayName("0 이하의 거리로 요금을 계산하면 예외가 발생한다.")
    @Test
    void create_distanceUnder0() {
        // given
        final FarePolicy policy = new FarePolicy();
        int distance = 0;

        // when, then
        assertThatThrownBy(() -> new Fare(policy, distance))
                .isInstanceOf(DistanceNotPositiveException.class)
                .hasMessage("거리는 0 이하일 수 없습니다.");
    }
}
