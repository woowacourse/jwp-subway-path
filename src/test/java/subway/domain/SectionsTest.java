package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.UnsupportedParameterException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SectionsTest {

    private final Station STATION1 = new Station("잠실새내");
    private final Station STATION2 = new Station("잠실");
    private final Station STATION3 = new Station("잠실나루");
    private final Distance DISTANCE1 = new Distance(10);
    private final Distance DISTANCE2 = new Distance(15);
    private final Section SECTION1 = new Section(STATION1, STATION2, DISTANCE1);
    private final Section SECTION2 = new Section(STATION2, STATION3, DISTANCE2);
    private final List<Section> SECTION_LIST = List.of(SECTION1, SECTION2);

    @DisplayName("Section 리스트를 입력받아 생성한다")
    @Test
    void 생성한다() {
        assertDoesNotThrow(() -> new Sections(SECTION_LIST));
    }

    @DisplayName("중간에 구간을 추가한다")
    @Test
    void 중간_구간을_추가한다() {
        //given
        Sections sections = new Sections(SECTION_LIST);
        Station station = new Station("강변");
        Section section = new Section(STATION2, station, new Distance(10));
        //when
        Sections editSections = sections.addSection(section);
        //then
        assertThat(editSections.getSections())
                .contains(SECTION1,
                        section,
                        new Section(station, STATION3, new Distance(5)));
    }

    @DisplayName("종점에 구간을 추가한다")
    @Test
    void 종점에_구간을_추가한다() {
        //given
        Sections sections = new Sections(SECTION_LIST);
        Station station = new Station("강변");
        Section section = new Section(station, STATION1, new Distance(10));
        //when
        Sections editSections = sections.addSection(section);
        //then
        assertThat(editSections.getSections())
                .contains(section, SECTION1, SECTION2);
    }

    @DisplayName("추가하려는 구간이 더 크면 예외를 발생시킨다")
    @Test
    void 구간_길이가_크면_예외를_발생한다() {
        //given
        Sections sections = new Sections(SECTION_LIST);
        Station station = new Station("강변");
        Section section = new Section(STATION1, station, new Distance(10));
        //then
        assertThatThrownBy(() -> sections.addSection(section))
                .isInstanceOf(UnsupportedParameterException.class);
    }

    // TODO: 5/18/23 현재 순환선은 가능함 이거 끊어줘야함
    @DisplayName("추가하려는 구간이 이미 구간들에 존재하면 예외를 발생한다")
    @Test
    void 이미_존재하는_구간은_예외를_발생한다() {
        //given
        Sections sections = new Sections(SECTION_LIST);
        Section section = new Section(STATION1, STATION3, new Distance(5));
        //then
        assertThatThrownBy(() -> sections.addSection(section)).isInstanceOf(UnsupportedParameterException.class);
    }

    @DisplayName("중간역을 삭제하면 양옆의 구간을 합친다")
    @Test
    void 중간역을_삭제하고_양옆_구간을_합친다() {
        //given
        Sections sections = new Sections(SECTION_LIST);
        //when
        Sections editSection = sections.removeStation(STATION2);
        //then
        assertThat(editSection.getSections())
                .contains(new Section(STATION1, STATION3, new Distance(25)));
    }

    @DisplayName("종점역을 삭제하면 해당 역이 연결된 구간을 없앤다")
    @Test
    void 종점역을_삭제하면_해당_구간_삭제한다() {
        //given
        Sections sections = new Sections(SECTION_LIST);
        //when
        Sections editSection = sections.removeStation(STATION1);
        //then
        assertThat(editSection.getSections())
                .contains(new Section(STATION2, STATION3, new Distance(15)));
    }

    @DisplayName("없는 역을 삭제하면 예외를 발생한다")
    @Test
    void 없는_역을_삭제하면_예외를_발생한다() {
        //given
        Sections sections = new Sections(SECTION_LIST);
        //then
        assertThatThrownBy(() -> sections.removeStation(new Station("없는역")))
                .isInstanceOf(UnsupportedParameterException.class);
    }

    @DisplayName("역을 순서대로 반환한다")
    @Test
    void 역을_순서대로_반환한다() {
        //given
        Sections sections = new Sections(SECTION_LIST);
        //when
        List<Station> allStationUpToDown = sections.findAllStationUpToDown();
        //then
        assertThat(allStationUpToDown)
                .containsExactly(STATION1, STATION2, STATION3);
    }

    @DisplayName("상행 종점을 반환한다")
    @Test
    void 상행_종점을_반환한다() {
        //given
        Sections sections = new Sections(SECTION_LIST);
        //when
        Station firstStation = sections.findFirstStation();
        //then
        assertThat(firstStation).isEqualTo(STATION1);
    }

    @DisplayName("하행 종점을 반환한다")
    @Test
    void 하행_종점을_반환한다() {
        //given
        Sections sections = new Sections(SECTION_LIST);
        //when
        Station lastStation = sections.findLastStation();
        //then
        assertThat(lastStation).isEqualTo(STATION3);
    }
}
