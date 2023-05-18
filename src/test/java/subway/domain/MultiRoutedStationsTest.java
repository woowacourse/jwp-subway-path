package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.LineFixture.FIXTURE_LINE_1;
import static subway.domain.LineFixture.FIXTURE_LINE_2;
import static subway.domain.SectionFixture.LINE1_SECTIONS;
import static subway.domain.SectionFixture.LINE2_SECTIONS;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MultiRoutedStationsTest {

    @DisplayName("노선 별 역 연결 정보를 전달받아 모든 노선의 역을 연결하는 그래프를 생성한다")
    @Test
    void create() {
        Map<Line, RoutedStations> sectionsByLine = Map.of(
                FIXTURE_LINE_1, RoutedStations.from(LINE1_SECTIONS),
                FIXTURE_LINE_2, RoutedStations.from(LINE2_SECTIONS)
        );

        MultiRoutedStations result = MultiRoutedStations.from(sectionsByLine);

        for (Entry<Line, RoutedStations> entry : sectionsByLine.entrySet()) {
            checkResult(entry.getKey(), entry.getValue().extractSections(), result);
        }
    }

    private void checkResult(final Line expectedLine,
                             final List<Section> expectedSections,
                             final MultiRoutedStations result) {
        for (Section section : expectedSections) {
            StationEdge edge = result.getEdgeByLine(section.getLeft(), section.getRight(), expectedLine).get();
            assertThat(edge.getLine())
                    .isEqualTo(expectedLine);
            assertThat(result.getEdgeWeight(edge))
                    .isEqualTo(section.getDistance().getValue());
        }
    }
}
