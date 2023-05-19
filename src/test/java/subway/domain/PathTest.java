package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PathTest {

    @ParameterizedTest
    @DisplayName("거리에 따른 요금 계산")
    @CsvSource(value = {"0:1250", "10:1250", "11:1350", "50:2050", "51:2150", "59:2250"}, delimiter = ':')
    void CalculateFee(final int distance, final int expected) {
        // given
        final Path path = new Path(List.of(), distance);

        // when & then
        assertThat(path.getFee()).isEqualTo(expected);
    }
}
