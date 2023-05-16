package subway.route.domain.jgraph;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class DistanceFareCalculatorTest {

    @Test
    void calculate() {
        final DistanceFareCalculator fareCalculator = new DistanceFareCalculator();

        assertAll(
                () -> assertThat(fareCalculator.calculate(0)).isEqualTo(0),
                () -> assertThat(fareCalculator.calculate(1)).isEqualTo(1250),
                () -> assertThat(fareCalculator.calculate(10)).isEqualTo(1250),
                () -> assertThat(fareCalculator.calculate(14)).isEqualTo(1250),
                () -> assertThat(fareCalculator.calculate(15)).isEqualTo(1350),
                () -> assertThat(fareCalculator.calculate(50)).isEqualTo(2050),
                () -> assertThat(fareCalculator.calculate(57)).isEqualTo(2050),
                () -> assertThat(fareCalculator.calculate(58)).isEqualTo(2150)
        );
    }
}
