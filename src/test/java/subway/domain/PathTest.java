package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.TestFixture.STATION_A;
import static subway.TestFixture.STATION_C;
import static subway.TestFixture.STATION_D;
import static subway.TestFixture.STATION_E;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathTest {

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

        assertThat(path.getDistance()).isEqualTo(new Distance(10));
    }
}
