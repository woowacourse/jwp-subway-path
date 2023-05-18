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
    @ParameterizedTest(name = "10km + {0}km = {1}원")
    @CsvSource({"4,1250", "5,1350", "6,1350", "10,1450", "40,2050"})
    void calculate_fare_between_10km_50km(int extraDistance, int fare) {
        var path = new Path(List.of(
                new Section(STATION_A, STATION_C, 10),
                new Section(STATION_C, STATION_D, extraDistance)
        ));

        assertThat(path.calculateFare()).isEqualTo(fare);
    }

    @DisplayName("50km 초과는 8km 당 100원이다")
    @ParameterizedTest(name = "10km + {0}km = {1}원")
    @CsvSource({"48,2150", "56, 2250", "64, 2350"})
    void calculate_fare_over_50km(int extraDistance, int fare) {
        var path = new Path(List.of(
                new Section(STATION_A, STATION_C, 10),
                new Section(STATION_C, STATION_D, extraDistance)
        ));

        assertThat(path.calculateFare()).isEqualTo(fare);
    }

    @DisplayName("하행 방향으로 중복 없이 역들을 꺼낸다")
    @Test
    void getDistinctStationsOrderedFromUpperToLower() {
        Path path = new Path(List.of(
                new Section(STATION_A, STATION_C, 2),
                new Section(STATION_C, STATION_D, 3),
                new Section(STATION_D, STATION_E, 5)
        ));

        List<Station> stations = path.getStations();

        assertThat(stations).containsExactly(STATION_A, STATION_C, STATION_D, STATION_E);
    }

    @DisplayName("거리를 구한다")
    @Test
    void getDistance() {
        Path path = new Path(List.of(
                new Section(STATION_A, STATION_C, 2),
                new Section(STATION_C, STATION_D, 3),
                new Section(STATION_D, STATION_E, 5)
        ));

        assertThat(path.getDistance()).isEqualTo(10);
    }
}
