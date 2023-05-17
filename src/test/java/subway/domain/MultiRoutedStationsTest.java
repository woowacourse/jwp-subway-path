package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.LineFixture.FIXTURE_LINE_1;
import static subway.domain.LineFixture.FIXTURE_LINE_2;
import static subway.domain.SectionFixture.LINE1_SECTION_ST1_ST2;
import static subway.domain.SectionFixture.LINE1_SECTION_ST2_ST3;
import static subway.domain.SectionFixture.LINE1_SECTION_ST3_ST4;
import static subway.domain.SectionFixture.LINE1_SECTION_ST4_ST5;
import static subway.domain.SectionFixture.LINE1_SECTION_ST5_ST6;
import static subway.domain.SectionFixture.LINE2_SECTION_ST1_ST8;
import static subway.domain.SectionFixture.LINE2_SECTION_ST7_ST1;
import static subway.domain.SectionFixture.LINE2_SECTION_ST8_ST9;
import static subway.domain.SectionFixture.LINE2_SECTION_ST9_ST10;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MultiRoutedStationsTest {

    @DisplayName("순서가 없는 구간 목록을 노선 별로 전달받아 모든 노선의 역을 연결하는 그래프를 생성한다")
    @Test
    void create() {
        Map<Line, RoutedStations> sectionsByLine = Map.of(
                FIXTURE_LINE_1, RoutedStations.from(List.of(
                        LINE1_SECTION_ST1_ST2,
                        LINE1_SECTION_ST2_ST3,
                        LINE1_SECTION_ST3_ST4,
                        LINE1_SECTION_ST4_ST5,
                        LINE1_SECTION_ST5_ST6
                )),
                FIXTURE_LINE_2, RoutedStations.from(
                        List.of(
                                LINE2_SECTION_ST7_ST1,
                                LINE2_SECTION_ST1_ST8,
                                LINE2_SECTION_ST8_ST9,
                                LINE2_SECTION_ST9_ST10
                        )
                )
        );

        MultiRoutedStations result = MultiRoutedStations.from(sectionsByLine);

        for (Entry<Line, RoutedStations> entry : sectionsByLine.entrySet()) {
            checkResult(entry.getKey(), entry.getValue().extractSections(), result);
        }
    }

    private void checkResult(final Line line, final List<Section> sections, final MultiRoutedStations result) {
        for (Section section : sections) {
            StationEdge edge = result.getEdgeByLine(section.getLeft(), section.getRight(), line).get();
            assertThat(edge.getLine())
                    .isEqualTo(line);
            assertThat(result.getEdgeWeight(edge))
                    .isEqualTo(section.getDistance().getValue());
        }
    }
}
