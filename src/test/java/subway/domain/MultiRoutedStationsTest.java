package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixture.LineFixture.FIXTURE_LINE_1;
import static subway.fixture.LineFixture.FIXTURE_LINE_2;
import static subway.fixture.SectionFixture.ROUTED_SECTIONS_1_TRANSFER_2_AT_ST1;
import static subway.fixture.SectionFixture.ROUTED_SECTIONS_2_TRANSFER_1_AT_ST1_3_AT_ST9;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.entity.Line;
import subway.domain.entity.Section;
import subway.domain.entity.Station;

@DisplayName("무방향 다중 역 연결 그래프 단위 테스트")
class MultiRoutedStationsTest {

    @DisplayName("노선 별 역 연결 정보를 전달받아 모든 노선의 역을 연결하는 그래프를 생성한다")
    @Test
    void create() {
        Map<Line, RoutedStations> sectionsByLine = Map.of(
                FIXTURE_LINE_1, RoutedStations.from(ROUTED_SECTIONS_1_TRANSFER_2_AT_ST1),
                FIXTURE_LINE_2, RoutedStations.from(ROUTED_SECTIONS_2_TRANSFER_1_AT_ST1_3_AT_ST9)
        );

        MultiRoutedStations result = MultiRoutedStations.from(sectionsByLine);

        // TODO 더 직관적으로 결과를 확인할 수 없을까?
        for (Entry<Line, RoutedStations> entry : sectionsByLine.entrySet()) {
            checkResult(entry.getKey(), entry.getValue().extractSections(), result);
        }
    }

    private void checkResult(final Line expectedLine,
                             final List<Section> expectedSections,
                             final MultiRoutedStations result) {
        for (Section section : expectedSections) {
            LineClassifiableSectionEdge edge = getEdgeByLine(section.getLeft(), section.getRight(), expectedLine,
                    result).get();
            assertThat(edge.getLine())
                    .isEqualTo(expectedLine);
            assertThat(result.getEdgeWeight(edge))
                    .isEqualTo(section.getDistance().getValue());
        }
    }

    private Optional<LineClassifiableSectionEdge> getEdgeByLine(final Station sourceVertex,
                                                                final Station targetVertex,
                                                                final Line line,
                                                                final MultiRoutedStations result) {
        Set<LineClassifiableSectionEdge> allEdges = result.getAllEdges(sourceVertex, targetVertex);
        if (allEdges.isEmpty()) {
            return Optional.empty();
        }
        return allEdges.stream()
                .filter(edge -> Objects.equals(edge.getLine(), line))
                .findFirst();
    }
}
