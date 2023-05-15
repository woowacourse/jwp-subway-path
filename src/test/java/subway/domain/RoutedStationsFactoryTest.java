package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.SectionFixture.SECTION_END;
import static subway.domain.SectionFixture.SECTION_MIDDLE_1;
import static subway.domain.SectionFixture.SECTION_MIDDLE_2;
import static subway.domain.SectionFixture.SECTION_MIDDLE_3;
import static subway.domain.SectionFixture.SECTION_START;

import java.util.List;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoutedStationsFactoryTest {

    @DisplayName("구간 정보를 전달받아 하행 기준으로 연결된 역 연결 정보를 생성한다")
    @Test
    void create() {
        List<Section> sections = List.of(
                SECTION_START,
                SECTION_MIDDLE_1,
                SECTION_MIDDLE_2,
                SECTION_MIDDLE_3,
                SECTION_END
        );

        SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> result = RoutedStationsFactory.create(sections);

        for (Section section : sections) {
            assertThat(result.containsEdge(section.getLeft(), section.getRight()))
                    .isTrue();
            assertThat(result.getEdgeWeight(result.getEdge(section.getLeft(), section.getRight())))
                    .isEqualTo(section.getDistance().getValue());
        }
    }
}
