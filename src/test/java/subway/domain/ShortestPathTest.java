package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ShortestPathTest {

    private Station station1;
    private Station station2;
    private Station station3;
    private ShortestPath shortestPath;

    @BeforeEach
    void setUp() {
        station1 = new Station("용산역");
        station2 = new Station("죽전역");
        station3 = new Station("감삼역");
        final Section section1 = new Section(new Station("용산역"), new Station("죽전역"), 10);
        final Section section2 = new Section(new Station("죽전역"), new Station("감삼역"), 5);
        shortestPath = new ShortestPath(new Sections(List.of(section1, section2)));
    }

    @Test
    @DisplayName("두 역 사이의 최단 경로를 구할 수 있다.")
    void findShortestPath() {
        // when
        final List<Station> shortestPath = this.shortestPath.findShortestPath(station1, station3);

        // then
        assertThat(shortestPath)
                .usingRecursiveComparison()
                .isEqualTo(List.of(station1, station2, station3));
    }

    @Test
    @DisplayName("두 역 사이 최단 경로의 거리를 구할 수 있다.")
    void getShortestPathDistance() {
        // when
        final int distance = (int) shortestPath.getDistance(station1, station3);

        // then
        assertThat(distance).isEqualTo(15);
    }
}
