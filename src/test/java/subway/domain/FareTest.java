package subway.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {
    @Test
    void sumFare() {
        //given
        Fare threeThousands = new Fare(3000);
        Fare twoHundreds = new Fare(200);
        //when
        Fare result = threeThousands.plus(twoHundreds);
        //then
        assertThat(result).isEqualTo(new Fare(3200));
    }

    @Test
    @DisplayName("")
    void cannotCreateFareWithNegativeValue() {
        //given
        //when
        //then
        assertThatThrownBy(() -> new Fare(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}