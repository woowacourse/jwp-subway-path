package subway.domain.fare.strategy.discount;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

import subway.domain.fare.FareInfo;
import subway.domain.subway.Line;

class AgeDiscountStrategyTest {

    @Test
    void discount() {
        AgeDiscountStrategy ageDiscountStrategy = new AgeDiscountStrategy();
        assertAll(
            () -> assertThat(ageDiscountStrategy.discount(1250, new FareInfo(10, Set.of(new Line()), 3))).isEqualTo(0),
            () -> assertThat(ageDiscountStrategy.discount(1250, new FareInfo(10, Set.of(new Line()), 8))).isEqualTo(
                450),
            () -> assertThat(ageDiscountStrategy.discount(1250, new FareInfo(10, Set.of(new Line()), 15))).isEqualTo(
                720),
            () -> assertThat(ageDiscountStrategy.discount(1250, new FareInfo(10, Set.of(new Line()), 20))).isEqualTo(
                1250));
    }
}
