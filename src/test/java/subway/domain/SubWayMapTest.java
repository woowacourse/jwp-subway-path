package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
class SubWayMapTest {
    private final Station JAMSIL = new Station(1L, "잠실");
    private final Station JAMSIL_NARU = new Station(2L, "잠실나루");
    private final Station GANGBYEON = new Station(3L, "강변");
    private final Station GUUI = new Station(4L, "구의");
    private final Station GUNDAE = new Station(5L, "건대입구");

    private final Section section1 = new Section(1L, JAMSIL, JAMSIL_NARU, new Distance(5));
    private final Section section2 = new Section(2L, JAMSIL_NARU, GANGBYEON, new Distance(6));
    private final Section section3 = new Section(3L, GANGBYEON, GUUI, new Distance(5));
    private final Section section4 = new Section(4L, GUUI, GUNDAE, new Distance(6));

    @DisplayName("두 역간 가장 짧은 구간을 계산한다")
    @Test
    void getShortestPath() {
        SubwayMap subwayMap = new SubwayMap(
                List.of(JAMSIL, JAMSIL_NARU, GANGBYEON, GUUI, GUNDAE),
                List.of(section1, section2, section3, section4));
        Sections shortestPath = subwayMap.getShortestPath(JAMSIL, GUNDAE);

        assertSoftly(softly -> {
            softly.assertThat(shortestPath.getSections()).hasSize(4);
            softly.assertThat(shortestPath.getSections()).containsExactly(section1, section2, section3, section4);
            softly.assertThat(shortestPath.getDistanceBetween(JAMSIL, GUNDAE).getValue()).isEqualTo(22);
        });
    }
}