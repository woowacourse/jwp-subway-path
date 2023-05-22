package subway.domain.path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.Sections;
import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class DijkstraShortestPathFinderTest {

    private TestDomainData dummyData;

    @BeforeEach
    void setUp() {
        dummyData = TestDomainData.create();
    }

    @Test
    @DisplayName("노량진 - 신촌")
    void case1() {
        // given
        final ShortestPathFinder shortestPathFinder = new DijkstraShortestPathFinder();
        final Sections sections = new Sections(dummyData.getSections());
        final Station start = dummyData.getStationByName("노량진");
        final Station end = dummyData.getStationByName("신촌");

        // when
        final Path path = shortestPathFinder.find(sections, start, end);

        // then
        final List<String> stations = path.getStations().stream()
                .map(Station::getName)
                .collect(Collectors.toUnmodifiableList());
        final List<String> expectedStationNames = List.of("노량진", "용산", "남영", "서울역", "시청", "충정로", "아현", "이대", "신촌");
        assertThat(stations).isEqualTo(expectedStationNames);
    }

    @Test
    @DisplayName("홍대입구 - 신길")
    void case2() {
        // given
        final ShortestPathFinder shortestPathFinder = new DijkstraShortestPathFinder();
        final Sections sections = new Sections(dummyData.getSections());
        final Station start = dummyData.getStationByName("홍대입구");
        final Station end = dummyData.getStationByName("신길");

        // when
        final Path path = shortestPathFinder.find(sections, start, end);

        // then
        final List<String> stations = path.getStations().stream()
                .map(Station::getName)
                .collect(Collectors.toUnmodifiableList());
        final List<String> expectedStationNames = List.of("홍대입구", "합정", "당산", "영등포구청", "문래", "신도림", "영등포", "신길");
        assertThat(stations).isEqualTo(expectedStationNames);
    }
}
