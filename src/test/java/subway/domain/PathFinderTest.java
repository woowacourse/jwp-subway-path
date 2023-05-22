package subway.domain;

import org.junit.jupiter.api.Test;
import subway.Fixture;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PathFinderTest {

    private final List<Station> stations = List.of(Fixture.stationA, Fixture.stationB, Fixture.stationC, Fixture.stationD, Fixture.stationE);
    private final List<Line> lines = List.of(Fixture.lineABC, Fixture.lineBDE);

    @Test
    void generateSubwayMap() {
        assertDoesNotThrow(() -> PathFinder.generate(stations, lines));
    }

    @Test
    void findPath() {
        final PathFinder pathFinder = PathFinder.generate(stations, lines);
        final Path path = pathFinder.findPath(Fixture.stationA, Fixture.stationE);

        assertAll(
                () -> assertThat(path.getPathStations()).containsExactly(Fixture.stationA, Fixture.stationB, Fixture.stationD, Fixture.stationE),
                () -> assertThat(path.getPathStations().size()).isEqualTo(4),
                () -> assertThat(path.getDistance()).isEqualTo(30)
        );
    }
}
