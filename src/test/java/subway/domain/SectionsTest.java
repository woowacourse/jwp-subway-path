package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.Fixture;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SectionsTest {
    @DisplayName("역 A와 B를 초기 역등록한다.")
    @Test
    void addInitStations() {
        // given
        final Sections sections = new Sections();

        // when
        sections.addInitStations(Fixture.stationA, Fixture.stationB, Fixture.distance2);

        // then
        final Section section = sections.getSections().get(0);
        assertThat(section.getUp()).isEqualTo(Fixture.stationA);
        assertThat(section.getDown()).isEqualTo(Fixture.stationB);
    }

    @DisplayName("구간 BC에 상행 종점 A를 추가하면 구간 AB가 생긴다")
    @Test
    void addUpEndpoint() {
        // given
        final Sections sections = new Sections(List.of(Fixture.sectionBC));

        // when
        sections.addUpEndpoint(Fixture.stationA, Fixture.distance1);

        // then
        final Section section = sections.getSections().get(0);
        assertThat(section.getUp()).isEqualTo(Fixture.stationA);
        assertThat(section.getDown()).isEqualTo(Fixture.stationB);
    }

    @DisplayName("구간 AB에 하행 종점 C를 추가하면 구간 BC가 생긴다")
    @Test
    void addDownEndpoint() {
        // given
        final Sections sections = new Sections(List.of(Fixture.sectionAB));

        // when
        sections.addDownEndpoint(Fixture.stationC, Fixture.distance1);

        // then
        final Section section = sections.getSections().get(1);
        assertThat(section.getUp()).isEqualTo(Fixture.stationB);
        assertThat(section.getDown()).isEqualTo(Fixture.stationC);
    }

    @DisplayName("구간 AB 중간에 C를 추가하면 구간 AB, BC가 생긴다")
    @Test
    void addIntermediate() {
        // given
        final Sections sections = new Sections(List.of(Fixture.sectionAB));

        // when
        sections.addIntermediate(Fixture.stationC, Fixture.stationA, Fixture.distance1);

        // then
        final Section prevToThis = sections.getSections().get(0);
        final Section thisToNext = sections.getSections().get(1);

        assertThat(prevToThis.getUp()).isEqualTo(Fixture.stationA);
        assertThat(prevToThis.getDown()).isEqualTo(Fixture.stationC);
        assertThat(thisToNext.getUp()).isEqualTo(Fixture.stationC);
        assertThat(thisToNext.getDown()).isEqualTo(Fixture.stationB);
    }

    @DisplayName("구간 중간의 역을 삭제하면 앞의 역과 다음 역이 이어지고 거리는 두 구간 거리의 합이다")
    @Test
    void deleteMid() {
        final Sections sections = new Sections(List.of(Fixture.sectionAB, Fixture.sectionBC));

        // when
        sections.delete(Fixture.stationB);

        //then
        Assertions.assertThat(sections.getSections()).contains(
                new Section(Fixture.stationA,
                        Fixture.stationC,
                        Fixture.sectionAB.getDistance().sum(Fixture.sectionBC.getDistance())
                )
        );
    }

    @DisplayName("역이 두 개이면 모든 구간을 삭제한다")
    @Test
    void deleteInit() {
        final Sections sections = new Sections(List.of(Fixture.sectionAB));

        // when
        sections.delete(Fixture.stationA);

        //then
        assertThat(sections.getSections().size()).isEqualTo(0);
    }

    @DisplayName("상행 종점을 삭제한다")
    @Test
    void deleteUp() {
        final Sections sections = new Sections(List.of(Fixture.sectionAB, Fixture.sectionBC));

        // when
        sections.delete(Fixture.stationA);

        //then
        Assertions.assertThat(sections.getSections()).containsOnly(Fixture.sectionBC);
    }

    @DisplayName("하행 종점을 삭제한다")
    @Test
    void deleteDown() {
        final Sections sections = new Sections(List.of(Fixture.sectionAB, Fixture.sectionBC));

        // when
        sections.delete(Fixture.stationC);

        //then
        Assertions.assertThat(sections.getSections()).containsOnly(Fixture.sectionAB);
    }


    @DisplayName("모든 역을 조회한다")
    @Test
    void getAllStations() {
        final Sections sections = new Sections(List.of(Fixture.sectionAB, Fixture.sectionBC));

        // when
        final List<Station> stations = sections.getAllStations();

        //then
        Assertions.assertThat(stations).containsOnly(Fixture.stationA, Fixture.stationB, Fixture.stationC);
    }

    @DisplayName("모든 구간의 거리를 조회한다")
    @Test
    void getAllDistances() {
        final Sections sections = new Sections(List.of(Fixture.sectionAB, Fixture.sectionBC));

        // when
        final List<Distance> distances = sections.getAllDistances();

        //then
        Assertions.assertThat(distances).containsOnly(Fixture.sectionAB.getDistance(), Fixture.sectionBC.getDistance());
    }
}