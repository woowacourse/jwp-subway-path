package subway.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subway.exception.DomainException;

class DistanceTest {

    @Test
    @DisplayName("양의 정수로 Distance 객체를 생성한다.")
    void distanceTest() {
        assertAll(
            () -> assertDoesNotThrow(() -> new Distance(1)),
            () -> assertDoesNotThrow(() -> new Distance(Integer.MAX_VALUE)));
    }

    @Test
    @DisplayName("양의 정수가 아니면 에러를 반환한다.")
    void distanceFailTest() {
        assertAll(
            () -> assertThatThrownBy(() -> new Distance(0)).isInstanceOf(DomainException.class),
            () -> assertThatThrownBy(() -> new Distance(-1)).isInstanceOf(DomainException.class));
    }
}
