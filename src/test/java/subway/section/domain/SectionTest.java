package subway.section.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.station.domain.Station;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
class SectionTest {
    @Test
    void Section_생성() {
        // given
        final String leftStationName = "강남역";
        final String rightStationName = "역삼역";
        final long distance = 3L;
        
        // when
        final Section section = new Section(leftStationName, rightStationName, distance, "1호선");
        
        // then
        assertThat(section).isNotNull();
    }
    
    @Test
    void 자신의_구간과_추가할_역이_왼쪽인_구간_생성() {
        // given
        final Section section = new Section("강남역", "역삼역", 3L, "1호선");
        final String base = "강남역";
        final String additional = "선릉역";
        final long distance = 5L;
        
        // when
        final Set<Section> createdLeftSection = section.getAdditionalSectionsWithOneself(base, Direction.LEFT, additional, distance);
        
        // then
        final Section additionalSection = new Section(additional, base, 5L, "1호선");
        assertThat(createdLeftSection).contains(section, additionalSection);
    }
    
    @Test
    void 자신의_구간과_추가할_역이_오른쪽인_구간_생성() {
        // given
        final Section section = new Section("강남역", "역삼역", 3L, "1호선");
        final String base = "역삼역";
        final String additional = "선릉역";
        final long distance = 5L;
        
        // when
        final Set<Section> createdLeftSection = section.getAdditionalSectionsWithOneself(base, Direction.RIGHT, additional, distance);
        
        // then
        final Section additionalSection = new Section(base, additional, 5L, "1호선");
        assertThat(createdLeftSection).contains(section, additionalSection);
    }
    
    @Test
    void 추가할_방향이_오른쪽일_때_분리된_구간을_생성() {
        // given
        final Section section = new Section("강남역", "역삼역", 5L, "1호선");
        final String base = "강남역";
        final String additional = "선릉역";
        final long distance = 2L;
        
        // when
        final Set<Section> createdLeftSection = section.getAdditionalSectionsWithOneself(base, Direction.RIGHT, additional, distance);
        
        // then
        final Section leftAdditionalSection = new Section(base, additional, 2L, "1호선");
        final Section rightAdditionalSection = new Section(additional, "역삼역", 3L, "1호선");
        assertThat(createdLeftSection).contains(leftAdditionalSection, rightAdditionalSection);
    }
    
    @Test
    void 추가할_방향이_왼쪽일_때_분리된_구간을_생성() {
        // given
        final Section section = new Section("강남역", "역삼역", 5L, "1호선");
        final String base = "역삼역";
        final String additional = "선릉역";
        final long distance = 2L;
        
        // when
        final Set<Section> createdLeftSection = section.getAdditionalSectionsWithOneself(base, Direction.LEFT, additional, distance);
        
        // then
        final Section leftAdditionalSection = new Section("강남역", additional, 2L, "1호선");
        final Section rightAdditionalSection = new Section(additional, base, 3L, "1호선");
        assertThat(createdLeftSection).contains(leftAdditionalSection, rightAdditionalSection);
    }
    
    @ParameterizedTest(name = "{displayName} : distance = {0}")
    @ValueSource(longs = {5, 6})
    void 분리될_Section이_추가될_Section보다_거리가_짧으면_예외_발생(final Long distance) {
        // given
        final Section section = new Section("강남역", "역삼역", 5L, "1호선");
        final String base = "역삼역";
        final String additional = "선릉역";
        
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> section.getAdditionalSectionsWithOneself(base, Direction.LEFT, additional, distance));
    }
    
    @Test
    void 그래프에_자신과_모든_역과_거리를_추가한다() {
        // given
        final Section section = new Section("강남역", "역삼역", 5L, "1호선");
        final WeightedMultigraph<Station, Section> graph = new WeightedMultigraph<>(Section.class);
        final DijkstraShortestPath<Station, Section> path = new DijkstraShortestPath<>(graph);
        
        // when
        section.addStationsAndDistanceToGraph(graph);
        
        // then
        final GraphPath<Station, Section> resultPath = path.getPath(new Station("강남역"), new Station("역삼역"));
        assertAll(
                () -> assertThat(resultPath.getVertexList()).containsExactly(new Station("강남역"), new Station("역삼역")),
                () -> assertThat(resultPath.getEdgeList()).containsExactly(section),
                () -> assertThat(resultPath.getWeight()).isEqualTo(5)
        );
    }
}
