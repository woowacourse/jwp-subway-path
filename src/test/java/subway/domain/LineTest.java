package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.Fixture;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class LineTest {

    @Test
    @DisplayName("노선에 첫 역 추가 기능")
    void addInitialSection() {
        // given
        final Line line = new Line("2호선", "green");

        // when & then
        assertDoesNotThrow(() -> line.addInitialSection(Fixture.stationA, Fixture.stationB, 10));
    }

    @Test
    @DisplayName("노선에 처음이 아닌 역 추가 기능")
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
    @DisplayName("노선에 역 ID로 역 삭제 기능")
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
