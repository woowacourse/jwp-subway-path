package subway.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SectionsRemoveTest {
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

    @DisplayName("구간이 하나밖에 없고 지울 Station 이 해당 구간에 포함되어 있으면 구간을 삭제한다")
    @Test
    void removeWhenOneSectionExists() {
        //given
        sections = new Sections(new ArrayList<>(List.of(section1)));

        //when
        sections.remove(station1);

        //then
        assertThat(sections.getSections().isEmpty()).isTrue();
    }

    @DisplayName("상행 종점을 지운다")
    @Test
    void removeUpEnd() {
        //given, when, then
        assertThat(sections.remove(station1)).isEqualTo(null);
        assertThat(sections.getSections().size()).isEqualTo(1);
    }

    @DisplayName("하행 종점을 지운다")
    @Test
    void removeDownEnd() {
        //given, when, then
        assertThat(sections.remove(station3)).isEqualTo(null);
        assertThat(sections.getSections().size()).isEqualTo(1);
    }

    @DisplayName("중간 역을 지운다")
    @Test
    void removeInside() {
        //given, when, then
        assertThat(sections.remove(station2))
                .isEqualTo(new Section(section1.getLine(),
                        section1.getPreStation(), section2.getStation(), section1.getDistance() + section2.getDistance()));
        assertThat(sections.getSections().size()).isEqualTo(1);
    }
}
