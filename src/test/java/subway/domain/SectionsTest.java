package subway.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.AddStationException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class SectionsTest {
    static Line line;
    static Station station1 = new Station("용산");
    static Station station2 = new Station("이촌");
    static Station station3 = new Station("서빙고");
    static Section section1;
    static Section section2;
    Sections sections;

    @BeforeAll
    static void setup() {
        Line line = new Line("경의중앙선", "청록");
        section1 = new Section(line, station1, station2, 10L);
        section2 = new Section(line, station2, station3, 10L);
    }

    @BeforeEach
    void beforeEach() {
        sections = new Sections(new ArrayList<>(List.of(section1, section2)));
    }

    @DisplayName("기존 구간이 비어있으면 구간을 등록한다")
    @Test
    void addFirst() {
        //given
        Sections sections = new Sections(new ArrayList<>());
        Section newSection = new Section(line, new Station("용산"), new Station("이촌"), 10L);

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
        Section newSection = new Section(line, station, station1, 10L);

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
        Section newSection = new Section(line, station3, station, 10L);

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
        Section newSection = new Section(line, station1, station, 5L);

        //when, then
        assertThat(sections.add(newSection)).isEqualTo(new Section(line, station, station2, 5L));
        assertThat(sections.getSections().contains(newSection)).isTrue();
    }

    @DisplayName("중간에 역을 추가한다(Station 이 존재할 때)")
    @Test
    void addInside_StationExists() {
        //given
        Station station = new Station("공덕");
        Section newSection = new Section(line, station, station3, 5L);

        //when, then
        assertThat(sections.add(newSection)).isEqualTo(new Section(line, station2, station, 5L));
        assertThat(sections.getSections().contains(newSection)).isTrue();
    }


    @DisplayName("거리 조건이 맞지 않으면 역 추가 시 예외가 발생한다")
    @Test
    void addInsideWithInvalidDistance() {
        //given
        Station station = new Station("옥수");
        Section newSection = new Section(line, station1, station, 10L);

        //when, then
        assertThatThrownBy(() -> sections.add(newSection))
                .isInstanceOf(AddStationException.class)
                .hasMessageContaining("기존 구간의 길이보다 큰 길이의 구간은 해당 구간 사이에 추가할 수 없습니다.");
    }

    @DisplayName("기존 구간에 포함되어 있는 노선을 추가하려 하면 예외가 발생한다")
    @Test
    void addExistSection() {
        //given,when,then
        assertThatThrownBy(() -> sections.add(section1))
                .isInstanceOf(AddStationException.class)
                .hasMessageContaining("추가할 수 없는 구간입니다");
    }
}
