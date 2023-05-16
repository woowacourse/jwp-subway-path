package subway.service.section.domain;

import org.junit.jupiter.api.Test;
import subway.service.station.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.StationFixture.GANGNAM;
import static subway.domain.StationFixture.JAMSIL;
import static subway.domain.StationFixture.JANGJI;
import static subway.domain.StationFixture.SEONLEUNG;

class JgraphtRouteTest {

    @Test
    void 최단경로를_구한다() {
        // given

        // gangnam ->(10) jangji ->(5) jamsil
        Section jangjiJamsil = new Section(JAMSIL, JANGJI, new Distance(5));
        Section gangnamJangji = new Section(JANGJI, GANGNAM, new Distance(10));

        // gangnam ->(3) seonleung ->(7) jamsil
        Section seonleungJamsil = new Section(JAMSIL, SEONLEUNG, new Distance(7));
        Section gangnamSeonleung = new Section(SEONLEUNG, GANGNAM, new Distance(3));

        List<Section> sections = List.of(jangjiJamsil, seonleungJamsil, gangnamJangji, gangnamSeonleung);
        JgraphtRoute shortestPath = JgraphtRoute.from(sections, GANGNAM, JAMSIL);

        List<Station> pathStations = shortestPath.getStations();
        double totalDistance = shortestPath.getDistance();

        assertThat(totalDistance).isEqualTo(10);
        assertThat(pathStations).containsExactlyInAnyOrder(GANGNAM, SEONLEUNG, JAMSIL);
    }

}
