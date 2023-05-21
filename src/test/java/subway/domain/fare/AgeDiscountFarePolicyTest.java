package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.path.Path;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AgeDiscountFarePolicyTest {

    private FarePolicy farePolicy = new AgeDiscountFarePolicy();

    @Test
    void 성인의_경우_할인이_적용되지_않는다() {
        // given
        final Path path = mock(Path.class);
        final Passenger passenger = new Passenger(19);
        final int fare = 1250;

        // when
        final int result = farePolicy.calculate(path, passenger, fare);

        // then
        assertThat(result).isEqualTo(1250);
    }

    @Test
    void 청소년의_경우_350원을_할인한_금액에서_20퍼센트가_할인된다() {
        // given
        final Path path = mock(Path.class);
        final Passenger passenger = new Passenger(18);
        final int fare = 1250;

        // when
        final int result = farePolicy.calculate(path, passenger, fare);

        // then
        assertThat(result).isEqualTo(720);
    }

    @Test
    void 어린이의_경우_350원을_할인한_금액에서_50퍼센트가_할인된다() {
        final Path path = mock(Path.class);
        final Passenger passenger = new Passenger(6);
        final int fare = 1250;

        // when
        final int result = farePolicy.calculate(path, passenger, fare);

        // then
        assertThat(result).isEqualTo(450);
    }

    @Test
    void 유아의_경우_무료다() {
        final Path path = mock(Path.class);
        final Passenger passenger = new Passenger(5);
        final int fare = 1250;

        // when
        final int result = farePolicy.calculate(path, passenger, fare);

        // then
        assertThat(result).isZero();
    }
}
