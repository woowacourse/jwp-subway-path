package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.path.Path;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AgeDiscountFarePolicyTest {

    private FarePolicy farePolicy = new AgeDiscountFarePolicy();

    @Test
    void 나이에_따라_금액을_계산한다() {
        // given
        final Path path = new Path(Collections.emptyList());
        final Passenger passenger = new Passenger(17);
        final int fare = 1250;

        // when
        final int result = farePolicy.calculate(path, passenger, fare);

        // then
        assertThat(result).isEqualTo(720);
    }
}
