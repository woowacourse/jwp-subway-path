package subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.SectionException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static subway.fixture.LineFixture.LINE_1;
import static subway.fixture.SectionFixture.SECTION_1;
import static subway.fixture.SectionFixture.SECTION_2;
import static subway.fixture.StationFixture.*;

class SectionsAddTest {
    Sections sections;

    @BeforeEach
    void beforeEach() {
        sections = new Sections(new ArrayList<>(List.of(SECTION_1, SECTION_2)));
    }

    @DisplayName("기존 구간이 비어있으면 구간을 등록한다")
    @Test
    void addFirst() {
        //given
        Sections sections = new Sections(new ArrayList<>());
        Section newSection = new Section(LINE_1, STATION_1, STATION_2, new Distance(10));

        //when
        sections.add(newSection);

        //then
        assertThat(sections.getSections().size()).isEqualTo(1);
    }

    @DisplayName("상행 종점을 추가한다")
    @Test
    void addUpEnd() {
        //given
        Station station = new Station("효창공원앞");
        Section newSection = new Section(LINE_1, station, STATION_1, new Distance(10));

        //when
        sections.add(newSection);

        //then
        assertThat(sections.getSections().size()).isEqualTo(3);
        assertThat(sections.getSections().contains(newSection)).isTrue();
    }

    @DisplayName("하행 종점을 추가한다")
    @Test
    void addDownEnd() {
        //given
        Station station = new Station("한남");
        Section newSection = new Section(LINE_1, STATION_3, station, new Distance(10));

        //when
        sections.add(newSection);

        //then
        assertThat(sections.getSections().size()).isEqualTo(3);
        assertThat(sections.getSections().contains(newSection)).isTrue();
    }

    @DisplayName("중간에 역을 추가한다(preStation 이 존재할 때)")
    @Test
    void addInside_PreStationExists() {
        //given
        Station station = new Station("옥수");
        Section newSection = new Section(LINE_1, STATION_1, station, new Distance(3));

        //when, then
        assertThat(sections.add(newSection)).isEqualTo(new Section(LINE_1, station, STATION_2, new Distance(2)));
        assertThat(sections.getSections().contains(newSection)).isTrue();
    }

    @DisplayName("중간에 역을 추가한다(Station 이 존재할 때)")
    @Test
    void addInside_StationExists() {
        //given
        Station station = new Station("공덕");
        Section newSection = new Section(LINE_1, station, STATION_3, new Distance(5));

        //when, then
        assertThat(sections.add(newSection)).isEqualTo(new Section(LINE_1, STATION_2, station, new Distance(3)));
        assertThat(sections.getSections().contains(newSection)).isTrue();
    }


    @DisplayName("거리 조건이 맞지 않으면 역 추가 시 예외가 발생한다")
    @Test
    void addInsideWithInvalidDistance() {
        //given
        Station station = new Station("옥수");
        Section newSection = new Section(LINE_1, STATION_1, station, new Distance(10));

        //when, then
        assertThatThrownBy(() -> sections.add(newSection))
                .isInstanceOf(SectionException.class)
                .hasMessageContaining("기존 구간의 길이보다 큰 길이의 구간은 해당 구간 사이에 추가할 수 없습니다.");
    }

    @DisplayName("기존 구간에 포함되어 있는 노선을 추가하려 하면 예외가 발생한다")
    @Test
    void addExistSection() {
        //given,when,then
        assertThatThrownBy(() -> sections.add(SECTION_1))
                .isInstanceOf(SectionException.class)
                .hasMessageContaining("이미 존재하는 구간입니다");
    }
}
