package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.fixture.RouteFixture.역삼_삼성_10;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BaseFarePolicyTest {

    private BaseFarePolicy baseFarePolicy;

    @BeforeEach
    void setUp() {
        baseFarePolicy = new BaseFarePolicy();
    }

    @Test
    void 기본요금을_반환한다() {
        // when
        Fare fare = baseFarePolicy.calculate(역삼_삼성_10.ROUTE, null, new Fare());

        // then
        assertThat(fare).isEqualTo(new Fare(1250));
    }
}
