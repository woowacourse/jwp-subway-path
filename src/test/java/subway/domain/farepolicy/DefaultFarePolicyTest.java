package subway.domain.farepolicy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.line.edge.StationEdge;
import subway.domain.path.SubwayPath;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultFarePolicyTest {

    @ParameterizedTest(name = "총 거리 : {0}, 총 요금 : {1}")
    @DisplayName("거리에 따라 요금을 구한다.")
    @CsvSource(value = {"9:1250", "12:1350", "15:1350", "16:1450", "58:2150"}, delimiter = ':')
    void calculate_fare_by_distance(final int distance, final int expectedFareValue) {
        // given
        FarePolicy farePolicy = new DefaultFarePolicy();
        final SubwayPath subwayPath = new SubwayPath();
        subwayPath.add(1L, new StationEdge(1L, 2L, distance));
        final Fare expectedFare = new Fare(expectedFareValue);

        // when
        Fare fare = farePolicy.calculate(subwayPath);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }

}
