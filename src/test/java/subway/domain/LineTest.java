package subway.domain;

import org.junit.jupiter.api.Test;
import subway.Fixture;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class LineTest {

    @Test
    void addInitialSection() {
        // given
        final Line line = new Line("2호선", "green");

        // when & then
        assertDoesNotThrow(() -> line.addInitialSection(Fixture.stationA, Fixture.stationB, 10));
    }

    @Test
    void addSection() {
        // given
        final Map<Station, Section> testMap = new HashMap<>();
        testMap.put(Fixture.stationA, Fixture.sectionAB);
        testMap.put(Fixture.stationB, Fixture.sectionBC);
        final SectionMap sectionMap = new SectionMap(testMap, Fixture.stationA);
        final Line line = new Line(1L, "2호선", "green", sectionMap);

        // when & then
        assertDoesNotThrow(() -> line.addSection(Fixture.stationD, Fixture.stationB, 5));
    }

    @Test
    void deleteStation() {
        // given
        final Map<Station, Section> testMap = new HashMap<>();
        testMap.put(Fixture.stationA, Fixture.sectionAB);
        testMap.put(Fixture.stationB, Fixture.sectionBC);
        final SectionMap sectionMap = new SectionMap(testMap, Fixture.stationA);
        final Line line = new Line(1L, "2호선", "green", sectionMap);

        // when & then
        assertDoesNotThrow(() -> line.deleteStation(Fixture.stationB));
    }
}
