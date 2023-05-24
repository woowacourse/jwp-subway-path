package subway.business.domain.subwaymap;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FareTest {

    @DisplayName("거리와 승객 종류에 알맞은 요금을 갖는다.")
    @ParameterizedTest
    @MethodSource("fareTestDate")
    void shouldReturnAppropriateFareForPassengerWhenInputDistanceAndPassenger(int distance, Passenger passenger,
                                                                              String expectedFareText) {
        Fare fare = Fare.of(distance, passenger);
        assertThat(fare.getMoney()).isEqualTo(expectedFareText);
    }

    static Stream<Arguments> fareTestDate() {
        return Stream.of(
                Arguments.of(10, Passenger.GENERAL, "1250"),
                Arguments.of(50, Passenger.GENERAL, "2050"),
                Arguments.of(89, Passenger.GENERAL, "2450"),
                Arguments.of(10, Passenger.YOUTH, "1000"),
                Arguments.of(50, Passenger.YOUTH, "1640"),
                Arguments.of(89, Passenger.YOUTH, "1960"),
                Arguments.of(10, Passenger.CHILDREN, "625"),
                Arguments.of(50, Passenger.CHILDREN, "1025"),
                Arguments.of(89, Passenger.CHILDREN, "1225"),
                Arguments.of(10, Passenger.SENIOR, "0"),
                Arguments.of(50, Passenger.SENIOR, "0"),
                Arguments.of(89, Passenger.SENIOR, "0")
        );
    }

}
