package subway.domain.path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.Section;
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
        final List<Section> sections = dummyData.getSections();
        final Station start = dummyData.getStationByName("노량진");
        final Station end = dummyData.getStationByName("신촌");

        // when
        final Path path = shortestPathFinder.find(sections, start, end);

        // then
        final List<Long> sectionIds = path.getSections().stream()
                .map(Section::getId)
                .collect(Collectors.toUnmodifiableList());
        final List<Long> expectedSectionIds = List.of(5L, 4L, 3L, 2L, 12L, 13L, 14L, 15L);

        assertThat(sectionIds).isEqualTo(expectedSectionIds);
    }

    @Test
    @DisplayName("홍대입구 - 신길")
    void case2() {
        // given
        final ShortestPathFinder shortestPathFinder = new DijkstraShortestPathFinder();
        final List<Section> sections = dummyData.getSections();
        final Station start = dummyData.getStationByName("홍대입구");
        final Station end = dummyData.getStationByName("신길");

        // when
        final Path path = shortestPathFinder.find(sections, start, end);

        // then
        final List<Long> sectionIds = path.getSections().stream()
                .map(Section::getId)
                .collect(Collectors.toUnmodifiableList());
        final List<Long> expectedSectionIds = List.of(17L, 18L, 19L, 20L, 21L, 9L, 8L);

        assertThat(sectionIds).isEqualTo(expectedSectionIds);
    }
}
