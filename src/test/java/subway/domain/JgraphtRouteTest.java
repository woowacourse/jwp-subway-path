package subway.domain;

import org.junit.jupiter.api.Test;
import subway.service.path.domain.JgraphtRoute;
import subway.service.path.dto.ShortestPath;
import subway.service.section.domain.Distance;
import subway.service.section.domain.Section;
import subway.service.station.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixture.StationFixture.GANGNAM;
import static subway.fixture.StationFixture.JAMSIL;
import static subway.fixture.StationFixture.JANGJI;
import static subway.fixture.StationFixture.SEONLEUNG;

@SuppressWarnings("NonAsciiCharacters")
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
        JgraphtRoute jgraphtRoute = new JgraphtRoute();
        ShortestPath shortestPath = jgraphtRoute.findShortestPath(sections, GANGNAM, JAMSIL);
        List<Station> pathStations = shortestPath.getStations();

        assertThat(pathStations).containsExactlyInAnyOrder(GANGNAM, SEONLEUNG, JAMSIL);
    }

}
