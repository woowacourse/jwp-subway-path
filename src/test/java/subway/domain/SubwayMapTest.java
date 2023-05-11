package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static subway.Fixture.*;

class SubwayMapTest {

    @Test
    @DisplayName("A, B 두 역 사이에 D역을 추가한다")
    void addIntermediateStation() {
        // given
        final SubwayMap subwayMap1 = new SubwayMap(new HashMap<>(subwayMap.getSubwayMap()), new HashMap<>(subwayMap.getEndpointMap()));

        final Station thisStation = new Station("D");
        final Section thisToA = new Section(1, thisStation, stationA, line);
        final Section thisToB = new Section(1, thisStation, stationB, line);

        // when
        subwayMap1.addIntermediateStation(thisToA, thisToB);

        // then
        final List<Section> sectionByStation = subwayMap1.findSectionByStation(stationA).getSections();
        Assertions.assertThat(sectionByStation).contains(thisToA.getReverse());
        Assertions.assertThat(sectionByStation).doesNotContain(sectionAB);
    }

    @Test
    @DisplayName("삽입하려는 D역과 A역의 거리와 D역과 B역의 거리의 합이 A역과 B역의 거리의 합과 다르면 예외가 발생한다")
    void addIntermediateStation_throws() {
        // given
        final SubwayMap subwayMap1 = new SubwayMap(new HashMap<>(subwayMap.getSubwayMap()), new HashMap<>(subwayMap.getEndpointMap()));

        final Station thisStation = new Station("D");
        final Section thisToA = new Section(2, thisStation, stationA, line);
        final Section thisToB = new Section(1, thisStation, stationB, line);

        // when & then
        assertThatThrownBy(() -> subwayMap1.addIntermediateStation(thisToA, thisToB))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("초기 라인에 두 개의 역을 추가한다.")
    void addInitialStations() {
        // given
        final Map<Station, Sections> map = new HashMap<>();
        final Map<Line, Station> lineMap = new HashMap<>();
        final SubwayMap subwayMap1 = new SubwayMap(map, lineMap);

        // when
        subwayMap1.addInitialStations(sectionAB, sectionBA);

        // then
        final List<Section> sectionsFromA = subwayMap1.findSectionByStation(stationA).getSections();
        final List<Section> sectionsFromB = subwayMap1.findSectionByStation(stationB).getSections();

        Assertions.assertThat(sectionsFromA).containsOnly(sectionAB);
        Assertions.assertThat(sectionsFromB).containsOnly(sectionBA);
    }

    @Test
    @DisplayName("라인이 초기 상태가 아닐 때 초기 라인에 두 개의 역을 추가하면 예외가 발생한다.")
    void addInitialStations_not_init_throw() {
        // given
        final SubwayMap subwayMap1 = new SubwayMap(new HashMap<>(subwayMap.getSubwayMap()), new HashMap<>(subwayMap.getEndpointMap()));

        // when
        assertThatThrownBy(() -> subwayMap1.addInitialStations(sectionAB, sectionBA))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상행 종점 역Z를 추가하면 기존 상행 종점역 A는 SectionAZ를, Z는 오직 SectionZA만 갖는다.")
    void addUpEndPointStation() {
        final SubwayMap subwayMap1 = new SubwayMap(new HashMap<>(subwayMap.getSubwayMap()), new HashMap<>(subwayMap.getEndpointMap()));
        // given
        final Station stationZ = new Station("Z");
        final Section sectionZA = new Section(1, stationZ, stationA, line);

        // when
        subwayMap1.addUpEndPoint(sectionZA);

        // then
        final List<Section> sectionsFromA = subwayMap1.findSectionByStation(stationA).getSections();
        final List<Section> sectionsFromZ = subwayMap1.findSectionByStation(stationZ).getSections();
        Assertions.assertThat(sectionsFromA).contains(sectionZA.getReverse());
        Assertions.assertThat(sectionsFromZ).containsOnly(sectionZA);
    }

    @Test
    @DisplayName("하행 종점 역Z를 추가하면 기존 하행 종점역 C는 SectionCZ를, Z는 오직 SectionZC만 갖는다.")
    void addDownEndPointStation() {
        // given
        final SubwayMap subwayMap1 = new SubwayMap(new HashMap<>(subwayMap.getSubwayMap()), new HashMap<>(subwayMap.getEndpointMap()));

        final Station stationZ = new Station("Z");
        final Section sectionZC = new Section(1, stationZ, stationC, line);

        // when
        subwayMap1.addDownEndPoint(sectionZC);

        // then
        final List<Section> sectionsFromC = subwayMap1.findSectionByStation(stationC).getSections();
        final List<Section> sectionsFromZ = subwayMap1.findSectionByStation(stationZ).getSections();
        Assertions.assertThat(sectionsFromC).contains(sectionZC.getReverse());
        Assertions.assertThat(sectionsFromZ).containsOnly(sectionZC);
    }

    @Test
    @DisplayName("종점 추가시 선택한 역이 종점이 아닐 경우 예외가 발생한다")
    void addEndPointStation_notEntPoint_throw() {
        // given
        final SubwayMap subwayMap1 = new SubwayMap(new HashMap<>(subwayMap.getSubwayMap()), new HashMap<>(subwayMap.getEndpointMap()));

        final Station stationZ = new Station("Z");
        final Section sectionZB = new Section(1, stationZ, stationB, line);

        // when & then
        assertThatThrownBy(() -> subwayMap1.addDownEndPoint(sectionZB))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("연결되지 않은 A, C 두 역 사이에 D역을 추가하면 예외가 발생한다")
    void addIntermediateStation_not_connected_throw() {
        // given
        final SubwayMap subwayMap1 = new SubwayMap(new HashMap<>(subwayMap.getSubwayMap()), new HashMap<>(subwayMap.getEndpointMap()));

        final Station thisStation = new Station("D");
        final Section thisToA = new Section(1, thisStation, stationA, line);
        final Section thisToC = new Section(1, thisStation, stationC, line);

        // when
        assertThatThrownBy(() -> subwayMap1.addIntermediateStation(thisToA, thisToC))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상행 종점을 삭제")
    void removeUpEndpoint() {
        // given
        final SubwayMap subwayMap1 = new SubwayMap(new HashMap<>(subwayMap.getSubwayMap()), new HashMap<>(subwayMap.getEndpointMap()));

        // when
        subwayMap1.deleteStation(stationA.getId());

        // then
        final List<Section> sectionList = subwayMap1.getSubwayMap().values().stream()
                .flatMap(sections -> sections.getSections().stream())
                .collect(Collectors.toList());
        Assertions.assertThat(sectionList).containsExactly(sectionBC, sectionCB);
    }

    @Test
    @DisplayName("하행 종점을 삭제")
    void removeDownEndpoint() {
        // given
        final SubwayMap subwayMap1 = new SubwayMap(new HashMap<>(subwayMap.getSubwayMap()), new HashMap<>(subwayMap.getEndpointMap()));

        // when
        subwayMap1.deleteStation(stationC.getId());

        // then
        final List<Section> sectionList = subwayMap1.getSubwayMap().values().stream()
                .flatMap(sections -> sections.getSections().stream())
                .collect(Collectors.toList());
        Assertions.assertThat(sectionList).containsExactly(sectionAB, sectionBA);
    }

    @Test
    @DisplayName("마지막 두 역 삭제")
    void removeLastStations() {
        // given
        final SubwayMap subwayMap1 = new SubwayMap(new HashMap<>(subwayMap.getSubwayMap()), new HashMap<>(subwayMap.getEndpointMap()));
        subwayMap1.deleteStation(stationC.getId());

        // when
        subwayMap1.deleteStation(stationA.getId());

        // then
        final List<Section> sectionList = subwayMap1.getSubwayMap().values().stream()
                .flatMap(sections -> sections.getSections().stream())
                .collect(Collectors.toList());
        Assertions.assertThat(sectionList.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("중간 역을 삭제")
    void removeMidStation() {
        // given
        final SubwayMap subwayMap1 = new SubwayMap(new HashMap<>(subwayMap.getSubwayMap()), new HashMap<>(subwayMap.getEndpointMap()));
        final Section sectionAC = new Section(3, stationA, stationC, line);
        final Section sectionCA = new Section(3, stationC, stationA, line);

        // when
        subwayMap1.deleteStation(stationB.getId());

        // then
        final List<Section> sectionList = subwayMap1.getSubwayMap().values().stream()
                .flatMap(sections -> sections.getSections().stream())
                .collect(Collectors.toList());
        sectionList.forEach(section -> System.out.println(section.getDeparture().getName() + section.getArrival().getName()));
        Assertions.assertThat(sectionList).containsExactly(sectionAC, sectionCA);
    }
}
