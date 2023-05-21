package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PassengerTest {

    @Test
    void 나이에_해당하는_AgeGroup을_반환한다() {
        // given
        final Passenger passenger = new Passenger(17);

        // when
        final AgeGroup result = passenger.calulateAgeGroup();

        // then
        assertThat(result).isEqualTo(AgeGroup.YOUTH);
    }
}
