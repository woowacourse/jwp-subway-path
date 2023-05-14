package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class SectionsTest {

    @Test
    @DisplayName("역 추가 등록 시 기준 역이 있어야 한다")
    void addSection() {
        // given
        Section section = new Section(1L, new Station("잠실"), new Station("역삼"), 10L);
        Sections sections = new Sections(List.of(section));
        Section newSection = new Section(1L, new Station("비버"), new Station("삼성"), 5L);

        assertThatThrownBy(
                () -> sections.addSection(newSection))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("접하는 역이 있는지 판별")
    void findEndPoint() {
        Section section1 = new Section(1L, new Station("잠실"), new Station("역삼"), 10L);
        Section section2 = new Section(1L, new Station("역삼"), new Station("선릉"), 9L);
        Section section3 = new Section(1L, new Station("비버"), new Station("라빈"), 10L);

        Sections sections = new Sections(Arrays.asList(section1, section2));

        assertThatThrownBy(
                () -> sections.addSection(section3))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("접하는 역이 없습니다.");
    }

    @Test
    @DisplayName("존재하는 구간인지 판별")
    void isSectionDuplicate() {
        Section section1 = new Section(1L, new Station("잠실"), new Station("역삼"), 10L);
        Section section2 = new Section(1L, new Station("비버"), new Station("라빈"), 10L);
        Section section3 = new Section(1L, new Station("비버"), new Station("라빈"), 10L);

        Sections sections = new Sections(Arrays.asList(section1, section2));

        assertThatThrownBy(
                () -> sections.addSection(section3))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("이미 존재하는 구간입니다.");
    }

    @Test
    @DisplayName("중간의 추가한 역이 잘들어가는지 테스트")
    void addStationTest() {
        Section section1 = new Section(1L, new Station("잠실"), new Station("역삼"), 10L);
        Section section2 = new Section(1L, new Station("역삼"), new Station("비버"), 10L);
        Section section3 = new Section(1L, new Station("라빈"), new Station("비버"), 9L);


        Sections sections = new Sections(Arrays.asList(section1, section2));
        sections.addSection(section3);

        assertAll(
                () -> assertThat(sections.getSortedStations())
                        .usingRecursiveComparison().isEqualTo(
                                List.of(
                                        new Station("잠실"),
                                        new Station("역삼"),
                                        new Station("라빈"),
                                        new Station("비버")
                                ))
        );
    }

    @Test
    @DisplayName("삭제를하면 역이 잘 이어지는지 테스트")
    void deleteStation() {
        Section section1 = new Section(1L, new Station("잠실"), new Station("역삼"), 10L);
        Section section2 = new Section(1L, new Station("역삼"), new Station("비버"), 10L);


        Sections sections = new Sections(Arrays.asList(section1, section2));
        sections.remove(new Station("역삼"));

        assertAll(
                () -> assertThat(sections.getSortedStations())
                        .usingRecursiveComparison().isEqualTo(
                                List.of(
                                        new Station("잠실"),
                                        new Station("비버")
                                ))
        );
    }
}