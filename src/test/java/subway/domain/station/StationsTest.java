package subway.domain.station;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class StationsTest {

    @Test
    void 최초_역_등록_테스트() {
        final Stations stations = new Stations();
        final Station from = new Station("from");
        final Station to = new Station("to");
        final StationDistance stationDistance = new StationDistance(5);

        stations.addInitialStations(from, to, stationDistance);

        final List<StationOnLine> actualStations = stations.getStations();
        assertThat(actualStations).hasSize(2);
        assertThat(actualStations.get(0).getNextStationDistance()).isEqualTo(stationDistance);
        assertThat(actualStations.get(1).getPreviousStationDistance()).isEqualTo(stationDistance);
    }
}
