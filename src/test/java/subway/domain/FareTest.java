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
    @DisplayName("음수로 생성할 수 없다.")
    void cannotCreateFareWithNegativeValue() {
        //given
        //when
        //then
        assertThatThrownBy(() -> new Fare(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("거리와 요금 단위거리로 요금을 추가할 수 있다.")
    void addFareForDistanceAndUnitDistance() {
        //given
        Fare fare = new Fare(3000);

        //when
        Distance additionalDistance = new Distance(120);
        Distance chargedUnitDistance = new Distance(7);
        Fare afterCharged = fare.addFareFor(additionalDistance, chargedUnitDistance, new Fare(100));

        //then
        assertThat(afterCharged).isEqualTo(new Fare(3000 + 1800));
    }
}