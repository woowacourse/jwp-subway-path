package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ActiveProfiles("test")
class DistanceTest {

    @DisplayName("생성 시 입력된 정수의 값을 가지는 Distance 인스턴스가 생성된다")
    @Test
    void createDistance() {
        Distance validDistance = new Distance(10);

        assertThat(validDistance.getValue()).isEqualTo(10);
    }

    @DisplayName("생성 시 0보다 작거나 같은 값이 들어오면 예외를 반환한다")
    @Test
    void throwExceptionWhenBelowZero() {
        assertThatThrownBy(() -> new Distance(0)).isInstanceOf(IllegalArgumentException.class);
    }
}
