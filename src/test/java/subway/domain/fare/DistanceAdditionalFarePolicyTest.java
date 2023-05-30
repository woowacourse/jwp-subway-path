package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.fixture.RouteFixture;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DistanceAdditionalFarePolicyTest {

    private DistanceAdditionalFarePolicy distanceAdditionalFarePolicy;

    @BeforeEach
    void setUp() {
        distanceAdditionalFarePolicy = new DistanceAdditionalFarePolicy();
    }

    @ParameterizedTest(name = "{0}km -> 추가요금: {1}원")
    @CsvSource(value = {"9:0", "10:0", "12:100", "15:100", "16:200", "50:800"}, delimiter = ':')
    void 기본_추가요금을_계산한다(final int distance, final int additionalFare) {
        // when
        Fare actual = distanceAdditionalFarePolicy.calculate(RouteFixture.getRouteDistanceOf(distance), null,
                new Fare());

        // then
        assertThat(actual).isEqualTo(new Fare(additionalFare));
    }

    @ParameterizedTest(name = "{0}km -> 추가요금: {1}원")
    @CsvSource(value = {"51:900", "58:900", "59:1000"}, delimiter = ':')
    void 초과_추가요금을_계산한다(final int distance, final int additionalFare) {
        // when
        Fare actual = distanceAdditionalFarePolicy.calculate(RouteFixture.getRouteDistanceOf(distance), null,
                new Fare());

        // then
        System.out.println(actual.getValue());
        assertThat(actual).isEqualTo(new Fare(additionalFare));
    }
}
