package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static subway.Fixture.*;

class SubwayMapTest {

    @Test
    @DisplayName("A, B 두 역 사이에 D역을 추가한다")
    void addIntermediateStation() {
        // given
        SubwayMap subwayMap1 = new SubwayMap(subwayMap.getSubwayMap());
        Station thisStation = new Station("D");
        Section thisToA = new Section(1, thisStation, stationA, line);
        Section thisToB = new Section(1, thisStation, stationB, line);

        // when
        subwayMap1.addIntermediateStation(thisToA, thisToB);

        // then
        List<Section> sectionByStation = subwayMap1.findSectionByStation(stationA).getSections();
        Assertions.assertThat(sectionByStation).contains(thisToA.getReverse());
        Assertions.assertThat(sectionByStation).doesNotContain(sectionAB);
    }

    @Test
    @DisplayName("삽입하려는 D역과 A역의 거리와 D역과 B역의 거리의 합이 A역과 B역의 거리의 합과 다르면 예외가 발생한다")
    void addIntermediateStation_throws() {
        // given
        SubwayMap subwayMap1 = new SubwayMap(subwayMap.getSubwayMap());

        Station thisStation = new Station("D");
        Section thisToA = new Section(2, thisStation, stationA, line);
        Section thisToB = new Section(1, thisStation, stationB, line);

        // when & then
        assertThatThrownBy(() -> subwayMap1.addIntermediateStation(thisToA, thisToB))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("초기 라인에 두 개의 역을 추가한다.")
    void addInitialStations() {
        // given
        Map<Station, Sections> map = new HashMap<>();
        SubwayMap subwayMap1 = new SubwayMap(map);

        // when
        subwayMap1.addInitialStations(sectionAB, sectionBA);

        // then
        List<Section> sectionsFromA = subwayMap1.findSectionByStation(stationA).getSections();
        List<Section> sectionsFromB = subwayMap1.findSectionByStation(stationB).getSections();

        Assertions.assertThat(sectionsFromA).containsOnly(sectionAB);
        Assertions.assertThat(sectionsFromB).containsOnly(sectionBA);
    }

    @Test
    @DisplayName("라인이 초기 상태가 아닐 때 초기 라인에 두 개의 역을 추가하면 예외가 발생한다.")
    void addInitialStations_not_init_throw() {
        // given
        SubwayMap subwayMap1 = new SubwayMap(subwayMap.getSubwayMap());

        // when
        assertThatThrownBy(() -> subwayMap1.addInitialStations(sectionAB, sectionBA))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상행 종점 역Z를 추가하면 기존 상행 종점역 A는 SectionAZ를, Z는 오직 SectionZA만 갖는다.")
    void addUpEndPointStation() {
        SubwayMap subwayMap1 = new SubwayMap(subwayMap.getSubwayMap());
        // given
        Station stationZ = new Station("Z");
        Section sectionZA = new Section(1, stationZ, stationA, line);

        // when
        subwayMap1.addEndPointStation(sectionZA);

        // then
        List<Section> sectionsFromA = subwayMap1.findSectionByStation(stationA).getSections();
        List<Section> sectionsFromZ = subwayMap1.findSectionByStation(stationZ).getSections();
        Assertions.assertThat(sectionsFromA).contains(sectionZA.getReverse());
        Assertions.assertThat(sectionsFromZ).containsOnly(sectionZA);
    }

    @Test
    @DisplayName("하행 종점 역Z를 추가하면 기존 하행 종점역 C는 SectionCZ를, Z는 오직 SectionZC만 갖는다.")
    void addDownEndPointStation() {
        // given
        SubwayMap subwayMap1 = new SubwayMap(subwayMap.getSubwayMap());
        Station stationZ = new Station("Z");
        Section sectionZC = new Section(1, stationZ, stationC, line);

        // when
        subwayMap1.addEndPointStation(sectionZC);

        // then
        List<Section> sectionsFromC = subwayMap1.findSectionByStation(stationC).getSections();
        List<Section> sectionsFromZ = subwayMap1.findSectionByStation(stationZ).getSections();
        Assertions.assertThat(sectionsFromC).contains(sectionZC.getReverse());
        Assertions.assertThat(sectionsFromZ).containsOnly(sectionZC);
    }

    @Test
    @DisplayName("종점 추가시 선택한 역이 종점이 아닐 경우 예외가 발생한다")
    void addEndPointStation_notEntPoint_throw() {
        // given
        SubwayMap subwayMap1 = new SubwayMap(subwayMap.getSubwayMap());
        Station stationZ = new Station("Z");
        Section sectionZB = new Section(1, stationZ, stationB, line);

        // when & then
        assertThatThrownBy(() -> subwayMap1.addEndPointStation(sectionZB))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("연결되지 않은 A, C 두 역 사이에 D역을 추가하면 예외가 발생한다")
    void addIntermediateStation_not_connected_throw() {
        // given
        SubwayMap subwayMap1 = new SubwayMap(subwayMap.getSubwayMap());
        Station thisStation = new Station("D");
        Section thisToA = new Section(1, thisStation, stationA, line);
        Section thisToC = new Section(1, thisStation, stationC, line);

        // when
        assertThatThrownBy(() -> subwayMap1.addIntermediateStation(thisToA, thisToC))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
