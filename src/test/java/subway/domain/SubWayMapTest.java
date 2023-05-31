package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.SubwayFixture.*;

@ActiveProfiles("test")
class SubWayMapTest {
    private final SubwayMap subwayMap = new SubwayMap(testStations, testSections);

    @DisplayName("두 역(잠실-건대)간 가장 짧은 구간을 계산한다")
    @Test
    void getShortestPathBetweenJamsilAndGundae() {
        Sections shortestPath = subwayMap.getShortestPath(JAMSIL, GUNDAE);

        assertSoftly(softly -> {
            softly.assertThat(shortestPath.getOrderedSections()).hasSize(4);
            softly.assertThat(shortestPath.getOrderedStations()).containsExactly(JAMSIL, SINDORIM, GANGBYEON, GUUI, GUNDAE);
            softly.assertThat(shortestPath.getDistanceBetween(JAMSIL, GUNDAE).getValue()).isEqualTo(20);
        });
    }

    @DisplayName("두 역(잠실-동작)간 가장 짧은 구간을 계산한다")
    @Test
    void getShortestPathBetweenJamsilAndDongJak() {
        Sections shortestPath = subwayMap.getShortestPath(JAMSIL, DONGJAK);

        assertSoftly(softly -> {
            softly.assertThat(shortestPath.getOrderedSections()).hasSize(6);
            softly.assertThat(shortestPath.getOrderedStations()).containsExactly(JAMSIL, SINDORIM, GANGBYEON, GUUI, YAKSOO, OKSOO, DONGJAK);
            softly.assertThat(shortestPath.getDistanceBetween(JAMSIL, DONGJAK).getValue()).isEqualTo(30);
        });
    }

    @DisplayName("두 역(잠실-사당)간 가장 짧은 구간을 계산한다")
    @Test
    void getShortestPathBetweenJamsilAndSadang() {
        Sections shortestPath = subwayMap.getShortestPath(JAMSIL, SADANG);

        assertSoftly(softly -> {
            softly.assertThat(shortestPath.getOrderedSections()).hasSize(8);
            softly.assertThat(shortestPath.getOrderedStations()).containsExactly(JAMSIL, SINDORIM, GANGBYEON, GUUI, YAKSOO, OKSOO, ISOO, JONGGAK, SADANG);
            softly.assertThat(shortestPath.getDistanceBetween(JAMSIL, SADANG).getValue()).isEqualTo(40);
        });
    }
}