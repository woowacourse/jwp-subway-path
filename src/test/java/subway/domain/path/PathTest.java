package subway.domain.path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PathTest {

    @ParameterizedTest(name = "{displayName}")
    @CsvSource(value = {"10, 1250", "16, 1450", "50, 2050", "58, 2150"})
    @DisplayName("경로 거리가 {0}이면 요금은 {1}원을 반환한다.")
    void calculate_fee(int distance, int expect) {
        // given
        Path path = new Path(List.of(), new Distance(distance));

        // when
        Fee result = path.calculateFee();

        // then
        assertThat(result.getFee()).isEqualTo(expect);

    }
}
