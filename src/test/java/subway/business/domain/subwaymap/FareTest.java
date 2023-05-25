package subway.business.domain.subwaymap;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixture.FixtureForSubwayMapTest.사호선;
import static subway.fixture.FixtureForSubwayMapTest.삼호선;
import static subway.fixture.FixtureForSubwayMapTest.이호선;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import subway.business.domain.line.Line;

class FareTest {

    @DisplayName("거리와 승객 종류에 알맞은 요금을 갖는다.")
    @ParameterizedTest
    @MethodSource("fareTestDate")
    void shouldReturnAppropriateFareForPassengerWhenInputDistanceAndPassenger(
            Line mostExpensiveLine,
            int distance,
            Passenger passenger,
            String expectedFareText
    ) {
        Fare fare = Fare.of(mostExpensiveLine, distance, passenger);
        assertThat(fare.getMoney()).isEqualTo(expectedFareText);
    }

    static Stream<Arguments> fareTestDate() {
        return Stream.of(
                Arguments.of(이호선, 10, Passenger.GENERAL, "1250"),
                Arguments.of(삼호선, 50, Passenger.GENERAL, "2150"),
                Arguments.of(사호선, 89, Passenger.GENERAL, "2650"),
                Arguments.of(이호선, 10, Passenger.YOUTH, "1000"),
                Arguments.of(삼호선, 50, Passenger.YOUTH, "1720"),
                Arguments.of(사호선, 89, Passenger.YOUTH, "2120"),
                Arguments.of(이호선, 10, Passenger.CHILDREN, "625"),
                Arguments.of(삼호선, 50, Passenger.CHILDREN, "1075"),
                Arguments.of(사호선, 89, Passenger.CHILDREN, "1325"),
                Arguments.of(이호선, 10, Passenger.SENIOR, "0"),
                Arguments.of(삼호선, 50, Passenger.SENIOR, "0"),
                Arguments.of(사호선, 89, Passenger.SENIOR, "0")
        );
    }

}
