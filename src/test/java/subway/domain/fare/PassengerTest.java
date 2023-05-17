package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PassengerTest {

    @Test
    void 나이에_따라_금액을_계산한다() {
        // given
        final Passenger passenger = new Passenger(17);
        final int fare = 1250;

        // when
        final int result = passenger.calculateFare(fare);

        // then
        assertThat(result).isEqualTo(720);
    }
}
