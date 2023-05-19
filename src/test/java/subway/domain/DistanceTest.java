package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.service.domain.Distance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceTest {

    @ParameterizedTest
    @DisplayName("거리는 양수가 아닌 경우 Distance 생성에 실패한다.")
    @ValueSource(ints = {-1, 0})
    void createDistance_fail(int input) {
        assertThatThrownBy(() -> Distance.from(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("거리가 양수인 경우 Distance 생성에 성공한다.")
    @ValueSource(ints = {1, 100})
    void createDistance_success(int input) {
        Distance distance = Distance.from(input);

        assertThat(distance.getValue()).isEqualTo(input);
    }

}
