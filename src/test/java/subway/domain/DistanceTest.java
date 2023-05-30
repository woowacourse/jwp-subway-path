package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.IllegalInputForDomainException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class DistanceTest {

    @DisplayName("생성한다.")
    @Test
    void create() {
        assertDoesNotThrow(() -> new Distance(5));
    }

    @DisplayName("거리가 0 이하면 예외를 발생한다.")
    @Test
    void throwExceptionWithZeroValue() {
        assertThatThrownBy(() -> new Distance(0)).isInstanceOf(IllegalInputForDomainException.class);
    }

    @DisplayName("두 거리의 합을 계산한다.")
    @Test
    void sum() {
        //given
        Distance distance1 = new Distance(10);
        Distance distance2 = new Distance(5);
        //when
        Distance sum = distance1.sum(distance2);
        //then
        assertThat(sum).isEqualTo(new Distance(15));
    }

    @DisplayName("두 거리를 입력 받아 합이 자기와 같으면 true를 반환한다.")
    @Test
    void equalToSumOf3And7() {
        //given
        Distance target = new Distance(10);
        Distance distance1 = new Distance(3);
        Distance distance2 = new Distance(7);
        //when
        boolean isSameValue = target.equalToSumOf(distance1, distance2);
        //then
        assertThat(isSameValue).isTrue();
    }

    @DisplayName("두 거리를 입력 받아 합이 자기와 같으면 false를 반환한다.")
    @Test
    void equalToSumOf3And6() {
        //given
        Distance target = new Distance(10);
        Distance distance1 = new Distance(3);
        Distance distance2 = new Distance(6);
        //when
        boolean isSameValue = target.equalToSumOf(distance1, distance2);
        //then
        assertThat(isSameValue).isFalse();
    }
}
