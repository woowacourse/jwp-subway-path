package subway.section.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.station.domain.Station;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
class SectionsTest {
    
    @Test
    void 최초_역_등록_시_두_역을_동시에_등록한다() {
        // given
        final Sections sections = new Sections();
        final String left = "잠실역";
        final String right = "선릉역";
        final long distance = 3L;
        
        // when
        sections.initAddStation(left, right, distance, "1호선");
        
        // then
        assertThat(sections.getSections()).contains(new Section(left, right, distance, "1호선"));
    }
    
    @Test
    void 가장_왼쪽에_역을_등록한다() {
        // given
        final String first = "잠실역";
        final String second = "가양역";
        final String third = "종합운동장";
        final String fourth = "선릉역";
        final long distance = 5L;
        
        final Section firstSection = new Section(first, second, distance, "1호선");
        final Section secondSection = new Section(second, third, distance, "1호선");
        final Section thirdSection = new Section(third, fourth, distance, "1호선");
        
        final Set<Section> initSections = Set.of(firstSection, secondSection, thirdSection);
        final Sections sections = new Sections(new HashSet<>(initSections));
        final String additionalStation = "화정역";
        final long additionalDistance = 3L;
        
        // when
        sections.addStation(first, Direction.LEFT, additionalStation, additionalDistance);
        
        // then
        final Section additionalSection = new Section(additionalStation, first, additionalDistance, "1호선");
        assertThat(sections.getSections()).contains(firstSection, secondSection, thirdSection, additionalSection);
    }
    
    @Test
    void 가장_오른쪽에_역을_등록한다() {
        // given
        final String first = "잠실역";
        final String second = "가양역";
        final String third = "종합운동장";
        final String fourth = "선릉역";
        final long distance = 5L;
        
        final Section firstSection = new Section(first, second, distance, "1호선");
        final Section secondSection = new Section(second, third, distance, "1호선");
        final Section thirdSection = new Section(third, fourth, distance, "1호선");
        
        final Set<Section> initSections = Set.of(firstSection, secondSection, thirdSection);
        final Sections sections = new Sections(new HashSet<>(initSections));
        final String additionalStation = "화정역";
        final long additionalDistance = 3L;
        
        // when
        sections.addStation(fourth, Direction.RIGHT, additionalStation, additionalDistance);
        
        // then
        final Section additionalSection = new Section(fourth, additionalStation, additionalDistance, "1호선");
        assertThat(sections.getSections()).contains(firstSection, secondSection, thirdSection, additionalSection);
    }
    
    @Test
    void 방향이_오른쪽일_때_base가_중간역이면_구간_사이에_역을_등록한다() {
        // given
        final String first = "잠실역";
        final String second = "가양역";
        final String third = "종합운동장";
        final String fourth = "선릉역";
        final long distance = 5L;
        
        final Section firstSection = new Section(first, second, distance, "1호선");
        final Section secondSection = new Section(second, third, distance, "1호선");
        final Section thirdSection = new Section(third, fourth, distance, "1호선");
        
        final Set<Section> initSections = Set.of(firstSection, secondSection, thirdSection);
        final Sections sections = new Sections(new HashSet<>(initSections));
        final String additionalStation = "화정역";
        final long additionalDistance = 3L;
        
        // when
        sections.addStation(first, Direction.RIGHT, additionalStation, additionalDistance);
        
        // then
        final Section additionalFirstSection = new Section(first, additionalStation, additionalDistance, "1호선");
        final Section additionalSecondSection = new Section(additionalStation, second, distance - additionalDistance, "1호선");
        assertThat(sections.getSections()).contains(secondSection, thirdSection, additionalFirstSection, additionalSecondSection);
    }
    
    @Test
    void 해당_노선의_역을_차례대로_정렬해서_반환하기() {
        // given
        final String first = "잠실역";
        final String second = "가양역";
        final String third = "종합운동장";
        final String fourth = "선릉역";
        final long distance = 5L;
        
        final Section firstSection = new Section(first, second, distance, "1호선");
        final Section secondSection = new Section(second, third, distance, "1호선");
        final Section thirdSection = new Section(third, fourth, distance, "1호선");
        
        final Set<Section> initSections = Set.of(firstSection, secondSection, thirdSection);
        final Sections sections = new Sections(new HashSet<>(initSections));
        final String additionalStation = "화정역";
        final long additionalDistance = 3L;
        sections.addStation(third, Direction.LEFT, additionalStation, additionalDistance);
        
        // when
        final List<String> sortedStations = sections.getSortedStations();
        
        // then
        assertThat(sortedStations).containsExactly("잠실역", "가양역", "화정역", "종합운동장", "선릉역");
    }
    
    @Test
    void 역이_2개만_존재하는_경우_모두_삭제한다() {
        // given
        final String first = "잠실역";
        final String second = "가양역";
        final long distance = 5L;
        
        final Section firstSection = new Section(first, second, distance, "1호선");
        
        final Set<Section> initSections = Set.of(firstSection);
        final Sections sections = new Sections(new HashSet<>(initSections));
        
        // when
        sections.removeStation(second);
        
        // then
        assertThat(sections.getSections()).isEmpty();
    }
    
    @Test
    void 가장_오른쪽_역을_삭제한다() {
        // given
        final String first = "잠실역";
        final String second = "가양역";
        final String third = "종합운동장";
        final String fourth = "선릉역";
        final long distance = 5L;
        
        final Section firstSection = new Section(first, second, distance, "1호선");
        final Section secondSection = new Section(second, third, distance, "1호선");
        final Section thirdSection = new Section(third, fourth, distance, "1호선");
        
        final Set<Section> initSections = Set.of(firstSection, secondSection, thirdSection);
        final Sections sections = new Sections(new HashSet<>(initSections));
        
        // when
        sections.removeStation(fourth);
        
        // then
        assertThat(sections.getSections()).contains(firstSection, secondSection);
    }
    
    @Test
    void 가장_왼쪽_역을_삭제한다() {
        // given
        final String first = "잠실역";
        final String second = "가양역";
        final String third = "종합운동장";
        final String fourth = "선릉역";
        final long distance = 5L;
        
        final Section firstSection = new Section(first, second, distance, "1호선");
        final Section secondSection = new Section(second, third, distance, "1호선");
        final Section thirdSection = new Section(third, fourth, distance, "1호선");
        
        final Set<Section> initSections = Set.of(firstSection, secondSection, thirdSection);
        final Sections sections = new Sections(new HashSet<>(initSections));
        
        // when
        sections.removeStation(first);
        
        // then
        assertThat(sections.getSections()).contains(secondSection, thirdSection);
    }
    
    @Test
    void 중간_역을_삭제한다() {
        // given
        final String first = "잠실역";
        final String second = "가양역";
        final String third = "종합운동장";
        final String fourth = "선릉역";
        final long distance = 5L;
        
        final Section firstSection = new Section(first, second, distance, "1호선");
        final Section secondSection = new Section(second, third, distance, "1호선");
        final Section thirdSection = new Section(third, fourth, distance, "1호선");
        
        final Set<Section> initSections = Set.of(firstSection, secondSection, thirdSection);
        final Sections sections = new Sections(new HashSet<>(initSections));
        
        // when
        sections.removeStation(second);
        
        // then
        final Section combinedSection = new Section(first, third, distance + distance, "1호선");
        assertThat(sections.getSections()).contains(thirdSection, combinedSection);
    }
    
    @Test
    void 최초_등록_시_구간이_하나라도_존재하면_예외_발생() {
        // given
        final Set<Section> initSections = Set.of(new Section("강남역", "역삼역", 5L, "1호선"));
        final Sections sections = new Sections(new HashSet<>(initSections));
        
        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> sections.initAddStation("선릉역", "강남역", 3L, "1호선"));
    }
    
    @Test
    void 기준역이_존재하지_않으면_예외_발생() {
        // given
        final Set<Section> initSections = Set.of(new Section("강남역", "역삼역", 5L, "1호선"));
        final Sections sections = new Sections(new HashSet<>(initSections));
        
        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> sections.addStation("선릉역", Direction.RIGHT, "가양역", 3L));
    }
    
    @Test
    void 추가하려는_역이_이미_존재하면_예외_발생() {
        // given
        final Set<Section> initSections = Set.of(new Section("강남역", "역삼역", 5L, "1호선"));
        final Sections sections = new Sections(new HashSet<>(initSections));
        
        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> sections.addStation("역삼역", Direction.RIGHT, "강남역", 3L));
    }
    
    @Test
    void 삭제하려는_역이_존재하지_않을_시_예외_발생() {
        // given
        final Set<Section> initSections = Set.of(new Section("강남역", "역삼역", 5L, "1호선"));
        final Sections sections = new Sections(new HashSet<>(initSections));
        
        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> sections.removeStation("가양역"));
    }
    
    @Test
    void 방향이_왼쪽일_때_base가_중간역이면_구간_사이에_역을_등록한다() {
        // given
        final String first = "잠실역";
        final String second = "가양역";
        final String third = "종합운동장";
        final String fourth = "선릉역";
        final long distance = 5L;
        
        final Section firstSection = new Section(first, second, distance, "1호선");
        final Section secondSection = new Section(second, third, distance, "1호선");
        final Section thirdSection = new Section(third, fourth, distance, "1호선");
        
        final Set<Section> initSections = Set.of(firstSection, secondSection, thirdSection);
        final Sections sections = new Sections(new HashSet<>(initSections));
        final String additionalStation = "화정역";
        final long additionalDistance = 3L;
        
        // when
        sections.addStation(third, Direction.LEFT, additionalStation, additionalDistance);
        
        // then
        final Section additionalFirstSection = new Section(second, additionalStation, additionalDistance, "1호선");
        final Section additionalSecondSection = new Section(additionalStation, third, distance - additionalDistance, "1호선");
        assertThat(sections.getSections()).contains(firstSection, thirdSection, additionalFirstSection, additionalSecondSection);
    }
    
    @Test
    void 전달받은_그래프에_자신의_모든_구간을_추가한다() {
        // given
        final String first = "잠실역";
        final String second = "가양역";
        final String third = "화정역";
        final String fourth = "종합운동장";
        final String fifth = "선릉역";
        
        final int distance1 = 3;
        final int distance2 = 2;
        final int distance3 = 6;
        final int distance4 = 7;
        final Section firstSection = new Section(first, second, distance1, "1호선");
        final Section secondSection = new Section(second, third, distance2, "1호선");
        final Section thirdSection = new Section(third, fourth, distance3, "1호선");
        final Section fourthSection = new Section(fourth, fifth, distance4, "1호선");
        
        final Set<Section> initSections = Set.of(firstSection, secondSection, thirdSection, fourthSection);
        final Sections sections = new Sections(new HashSet<>(initSections));
        
        final WeightedMultigraph<Station, Section> graph = new WeightedMultigraph<>(Section.class);
        final DijkstraShortestPath<Station, Section> path = new DijkstraShortestPath<>(graph);
        
        // when
        sections.addSectionsToGraph(graph);
        
        // then
        final GraphPath<Station, Section> resultPath = path.getPath(new Station("선릉역"), new Station("가양역"));
        assertAll(
                () -> assertThat(resultPath.getVertexList().stream().map(Station::getName))
                        .containsExactly("선릉역", "종합운동장", "화정역", "가양역"),
                () -> assertThat(resultPath.getEdgeList()).containsExactly(fourthSection, thirdSection, secondSection),
                () -> assertThat(resultPath.getWeight()).isEqualTo(distance4 + distance3 + distance2)
        );
    }
    
    @ParameterizedTest(name = "{displayName} : stationName = {0}, expectResult = {1}")
    @CsvSource(value = {"잠실역,true", "청라역,false"})
    void 해당_역이_포함되어있는지_확인(final String stationName, final boolean expectResult) {
        // given
        final String first = "잠실역";
        final String second = "가양역";
        final String third = "화정역";
        
        final int distance1 = 3;
        final int distance2 = 2;
        final Section firstSection = new Section(first, second, distance1, "1호선");
        final Section secondSection = new Section(second, third, distance2, "1호선");
        final Set<Section> initSections1 = new HashSet<>(Set.of(firstSection, secondSection));
        
        final Sections sections = new Sections(initSections1);
        
        // when
        final boolean isContainsStation = sections.isContainsStation(stationName);
        
        // then
        assertThat(isContainsStation).isEqualTo(expectResult);
    }
}
