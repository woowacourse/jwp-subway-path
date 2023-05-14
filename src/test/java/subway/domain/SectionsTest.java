package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.Fixture;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SectionsTest {

    @Test
    @DisplayName("역 A와 B를 초기 역등록한다.")
    void addInitStations() {
        // given
        Sections sections = new Sections();

        // when
        sections.addInitStations(Fixture.stationA, Fixture.stationB, Fixture.distance2);

        // then
        Section section = sections.getSections().get(0);
        assertThat(section.getUp()).isEqualTo(Fixture.stationA);
        assertThat(section.getDown()).isEqualTo(Fixture.stationB);
    }

    @Test
    @DisplayName("구간 BC에 상행 종점 A를 추가하면 구간 AB가 생긴다")
    void addUpEndpoint() {
        // given
        Sections sections = new Sections(List.of(Fixture.sectionBC));

        // when
        sections.addUpEndpoint(Fixture.stationA, Fixture.distance1);

        // then
        Section section = sections.getSections().get(0);
        assertThat(section.getUp()).isEqualTo(Fixture.stationA);
        assertThat(section.getDown()).isEqualTo(Fixture.stationB);
    }

    @Test
    @DisplayName("구간 AB에 하행 종점 C를 추가하면 구간 BC가 생긴다")
    void addDownEndpoint() {
        // given
        Sections sections = new Sections(List.of(Fixture.sectionAB));

        // when
        sections.addDownEndpoint(Fixture.stationC, Fixture.distance1);

        // then
        Section section = sections.getSections().get(1);
        assertThat(section.getUp()).isEqualTo(Fixture.stationB);
        assertThat(section.getDown()).isEqualTo(Fixture.stationC);
    }

    @Test
    @DisplayName("구간 AB 중간에 C를 추가하면 구간 AB, BC가 생긴다")
    void addIntermediate() {
        // given
        Sections sections = new Sections(List.of(Fixture.sectionAB));

        // when
        sections.addIntermediate(Fixture.stationC, Fixture.stationA, Fixture.distance1);

        // then
        Section prevToThis = sections.getSections().get(0);
        Section thisToNext = sections.getSections().get(1);

        assertThat(prevToThis.getUp()).isEqualTo(Fixture.stationA);
        assertThat(prevToThis.getDown()).isEqualTo(Fixture.stationC);
        assertThat(thisToNext.getUp()).isEqualTo(Fixture.stationC);
        assertThat(thisToNext.getDown()).isEqualTo(Fixture.stationB);
    }
}