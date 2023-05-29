package subway.domain.path;

import org.junit.jupiter.api.Test;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.Stations;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShortestPathFinderTest {

    @Test
    void 최단경로를_찾는다() {
        ShortestPathFinder shortestPathFinder = new ShortestPathFinder();

        // 2호선
        Station 잠실 = new Station("잠실");
        Station 잠실나루 = new Station("잠실나루");
        Station 건대입구 = new Station("건대입구");
        Station 홍대입구 = new Station("홍대입구");
        Station 수색 = new Station("수색");

        HashSet<Station> hashSet = new HashSet<>();
        hashSet.add(잠실);
        hashSet.add(잠실나루);
        hashSet.add(건대입구);
        hashSet.add(홍대입구);
        hashSet.add(수색);

        Stations stations = new Stations(hashSet);
        Section 잠실_잠실나루 = new Section(잠실, 잠실나루, 5);
        Section 잠실나루_건대입구 = new Section(잠실나루, 건대입구, 5);
        Section 건대입구_홍대입구 = new Section(건대입구, 홍대입구, 5);

        // 경중선
        Section 잠실_수색 = new Section(잠실, 수색, 100);
        Section 수색_홍대입구 = new Section(수색, 홍대입구, 100);

        Sections sections = new Sections(List.of(잠실_잠실나루, 잠실나루_건대입구, 건대입구_홍대입구, 잠실_수색, 수색_홍대입구));

        ShortestPath shortestPath = shortestPathFinder.findShortestPath(sections, stations, 잠실, 홍대입구);

        assertThat(shortestPath.getStations())
                .isEqualTo(List.of(잠실, 잠실나루, 건대입구, 홍대입구));
    }
}