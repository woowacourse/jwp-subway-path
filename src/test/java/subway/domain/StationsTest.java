package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.service.domain.Station;
import subway.service.domain.Stations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StationsTest {

    @Test
    @DisplayName("Stations 를 생성한다.")
    void createStations() {
        Station first = new Station("first");
        Station second = new Station("second");

        Stations stations = new Stations(List.of(first, second));

        assertThat(stations.getStations().get(0)).isEqualTo(first);
        assertThat(stations.getStations().get(1)).isEqualTo(second);
    }

}
