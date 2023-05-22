package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.fixture.RouteFixture.역삼_삼성_10;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AssembleFarePolicyTest {

    private AssembleFarePolicy assembleFarePolicy;

    @BeforeEach
    void setUp() {
        assembleFarePolicy = new AssembleFarePolicy(List.of(
                (route, age, fare) -> new Fare(1000),
                (route, age, fare) -> new Fare(2000)
        ));
    }

    @Test
    void 최종적으로_반환되는_요금으로_책정한다() {
        // when
        Fare fare = assembleFarePolicy.calculate(역삼_삼성_10.ROUTE, null, new Fare());

        // then
        assertThat(fare).isEqualTo(new Fare(2000));
    }
}
