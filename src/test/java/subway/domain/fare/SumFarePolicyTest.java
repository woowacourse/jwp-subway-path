package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.fixture.RouteFixture.역삼_삼성_10;

class SumFarePolicyTest {

    private SumFarePolicy sumFarePolicy;

    @BeforeEach
    void setUp() {
        sumFarePolicy = new SumFarePolicy(List.of(
                route -> new Fare(1000),
                route -> new Fare(2000)
        ));
    }

    @Test
    void 정책의_요금들을_합산한다() {
        // when
        Fare fare = sumFarePolicy.calculate(역삼_삼성_10.ROUTE);

        // then
        assertThat(fare).isEqualTo(new Fare(3000));
    }
}
