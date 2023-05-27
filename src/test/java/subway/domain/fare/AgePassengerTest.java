package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
class AgePassengerTest {

    @ParameterizedTest
    @CsvSource(value = {"3:1", "6:0.5", "12:0.5", "13:0.8", "18:0.8", "20:1"}, delimiter = ':')
    void 나이에따른_할인_비율_테스트(final int age, final double discountProportion) {
        final double discountProportionByAge = AgePassenger.findDiscountProportionByAge(age);

        assertThat(discountProportionByAge).isEqualTo(discountProportion);
    }
}
