package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FareTest {

    @ParameterizedTest
    @CsvSource({"9, 1250", "10, 1250", "12, 1350", "15, 1350", "19, 1450", "30, 1650", "50, 2050", "58, 2150",
            "65, 2250", "66, 2250", "74, 2350"})
    void 거리에_따른_요금을_올바르게_반환한다(int distance, int fare) {
        // given
        assertThat(Fare.of(new Distance(distance)).getFare()).isEqualTo(fare);
    }
}
