package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.TestFixture.STATION_A;
import static subway.TestFixture.STATION_C;
import static subway.TestFixture.STATION_D;
import static subway.TestFixture.STATION_E;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PathTest {

    @DisplayName("10km 이내는 운임이 1250원이다.")
    @Test
    void calculate_fare_under_10km() {
        var path = new Path(List.of(
                new Section(STATION_A, STATION_C, 2),
                new Section(STATION_C, STATION_D, 3),
                new Section(STATION_D, STATION_E, 5)
        ));

        assertThat(path.calculateFare()).isEqualTo(1250);
    }

    @DisplayName("10km~50km는 5km 당 100원이다")
    @ParameterizedTest
    @CsvSource({"4,1250", "5,1350", "6,1350", "10,1450", "40,2050"})
    void calculate_fare_between_10km_50km(int extraDistance, int fare) {
        var path = new Path(List.of(
                new Section(STATION_A, STATION_C, 10),
                new Section(STATION_C, STATION_D, extraDistance)
        ));

        assertThat(path.calculateFare()).isEqualTo(fare);
    }

    @DisplayName("50km 초과는 8km 당 100원이다")
    @ParameterizedTest
    @CsvSource({"48,2150", "56, 2250", "64, 2350"})
    void calculate_fare_over_50km(int extraDistance, int fare) {
        var path = new Path(List.of(
                new Section(STATION_A, STATION_C, 10),
                new Section(STATION_C, STATION_D, extraDistance)
        ));

        assertThat(path.calculateFare()).isEqualTo(fare);
    }
}
