package subway.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class LineTest {

    private final Section SECTION = new Section(new Station("잠실나루"), new Station("잠실새내"), new Distance(10));

    @DisplayName("생성한다")
    @Test
    void create() {
        assertDoesNotThrow(
                () -> new Line (
                        new LineName("2호선"),
                        new LineColor("초록"),
                        SECTION.getUpStation(),
                        SECTION.getDownStation(),
                        List.of(SECTION)
                )
        );
    }

    @DisplayName("중간 구간을 추가한다.")
    @Test
    void addSection() {
        //given
        Section newSection1 = new Section(new Station("잠실나루"), new Station("잠실"), new Distance(3));
        Section newSection2 = new Section(new Station("잠실"), new Station("잠실새내"), new Distance(7));
        Line line = new Line (
                new LineName("2호선"),
                new LineColor("초록"),
                SECTION.getUpStation(),
                SECTION.getDownStation(),
                List.of(SECTION)
        );
        //when
        Line afterLine = line.addMiddleSection(newSection1, newSection2);
        //then
        assertThat(afterLine.getSections()).contains(newSection1, newSection2);
    }

    @DisplayName("중간 구간을 추가할 수 없는 경우 예외를 발생시킨다.")
    @ParameterizedTest
    @MethodSource("sectionsDummy")
    void addSectionThrowException(final Section section1, final Section section2) {
        //given
        Line line = new Line (
                new LineName("2호선"),
                new LineColor("초록"),
                SECTION.getUpStation(),
                SECTION.getDownStation(),
                List.of(SECTION)
        );
        //then
        assertThatThrownBy(() -> line.addMiddleSection(section1, section2)).isInstanceOf(IllegalArgumentException.class);
    }

    static Stream<Arguments> sectionsDummy() {
        return Stream.of(
                Arguments.arguments(
                        new Section(new Station("삼성"), new Station("잠실"), new Distance(3)),
                        new Section(new Station("잠실"), new Station("잠실새내"), new Distance(7))
                ),
                Arguments.arguments(
                        new Section(new Station("잠실나루"), new Station("잠실"), new Distance(3)),
                        new Section(new Station("잠실"), new Station("구의"), new Distance(7))
                ),
                Arguments.arguments(
                        new Section(new Station("잠실나루"), new Station("잠실"), new Distance(2)),
                        new Section(new Station("잠실"), new Station("잠실새내"), new Distance(7))
                ),
                Arguments.arguments(
                        new Section(new Station("잠실나루"), new Station("잠실"), new Distance(3)),
                        new Section(new Station("잠실"), new Station("잠실새내"), new Distance(8))
                )
        );
    }

    @DisplayName("상행 종점 구간을 추가한다.")
    @Test
    void addFirstSection() {
        //given
        Section newSection = new Section(new Station("삼성"), new Station("잠실나루"), new Distance(3));
        Line line = new Line (
                new LineName("2호선"),
                new LineColor("초록"),
                SECTION.getUpStation(),
                SECTION.getDownStation(),
                List.of(SECTION)
        );
        //when
        Line afterLine = line.addFirstSection(newSection);
        //then
        assertThat(afterLine.getFirstStation()).isEqualTo(new Station("삼성"));
    }

    @DisplayName("하행 종점 구간을 추가한다.")
    @Test
    void addLastSection() {
        //given
        Section newSection = new Section(new Station("잠실새내"), new Station("구의"), new Distance(3));
        Line line = new Line (
                new LineName("2호선"),
                new LineColor("초록"),
                SECTION.getUpStation(),
                SECTION.getDownStation(),
                List.of(SECTION)
        );
        //when
        Line afterLine = line.addLastSection(newSection);
        //then
        assertThat(afterLine.getLastStation()).isEqualTo(new Station("구의"));
    }

    @DisplayName("초기 구간을 추가한다")
    @Test
    void addInitSection() {
        //given
        Line line = new Line (
                new LineName("2호선"),
                new LineColor("초록")
        );
        Section newSection = new Section(new Station("잠실새내"), new Station("구의"), new Distance(3));
        //when
        Line afterLine = line.addInitSection(newSection);
        //then
        assertAll(
                () -> assertThat(afterLine.getFirstStation()).isEqualTo(new Station("잠실새내")),
                () -> assertThat(afterLine.getLastStation()).isEqualTo(new Station("구의"))
        );
    }

    @DisplayName("중간 역을 삭제한다.")
    @Test
    void removeStation() {
        //given
        Section section1 = new Section(new Station("잠실나루"), new Station("잠실"), new Distance(3));
        Section section2 = new Section(new Station("잠실"), new Station("잠실새내"), new Distance(7));
        Line line = new Line (
                new LineName("2호선"),
                new LineColor("초록"),
                section1.getUpStation(),
                section2.getDownStation(),
                List.of(section1, section2)
        );
        Station target = new Station("잠실");
        //when
        Line afterLine = line.removeStation(target);
        //then
        assertThat(afterLine.getSections())
                .containsOnly(
                        new Section(
                                new Station("잠실나루"),
                                new Station("잠실새내"),
                                new Distance(10)));
    }

    @DisplayName("하행종점 삭제한다.")
    @Test
    void removeFirstStation() {
        //given
        Section section1 = new Section(new Station("잠실나루"), new Station("잠실"), new Distance(3));
        Section section2 = new Section(new Station("잠실"), new Station("잠실새내"), new Distance(7));
        Line line = new Line (
                new LineName("2호선"),
                new LineColor("초록"),
                section1.getUpStation(),
                section2.getDownStation(),
                List.of(section1, section2)
        );
        Station target = new Station("잠실나루");
        //when
        Line afterLine = line.removeStation(target);
        //then
        assertAll(
                () -> assertThat(afterLine.getFirstStation()).isEqualTo(new Station("잠실")),
                () -> assertThat(afterLine.getLastStation()).isEqualTo(new Station("잠실새내"))
        );
    }

    @DisplayName("상행종점을 삭제한다.")
    @Test
    void removeLastStation() {
        //given
        Section section1 = new Section(new Station("잠실나루"), new Station("잠실"), new Distance(3));
        Section section2 = new Section(new Station("잠실"), new Station("잠실새내"), new Distance(7));
        Line line = new Line (
                new LineName("2호선"),
                new LineColor("초록"),
                section1.getUpStation(),
                section2.getDownStation(),
                List.of(section1, section2)
        );
        Station target = new Station("잠실새내");
        //when
        Line afterLine = line.removeStation(target);
        //then
        assertAll(
                () -> assertThat(afterLine.getFirstStation()).isEqualTo(new Station("잠실나루")),
                () -> assertThat(afterLine.getLastStation()).isEqualTo(new Station("잠실"))
        );
    }

    @DisplayName("한 구간만 노선에 존재하는 경우 한 역만 삭제해도 이어진 두 역이 다 지워진다.")
    @Test
    void removeStationWhenOnlyOneSectionExist() {
        //given
        Section section = new Section(new Station("잠실나루"), new Station("잠실"), new Distance(3));
        Line line = new Line (
                new LineName("2호선"),
                new LineColor("초록"),
                section.getUpStation(),
                section.getDownStation(),
                List.of(section)
        );
        Station target1 = new Station("잠실나루");
        Station target2 = new Station("잠실");
        //when
        Line afterLine1 = line.removeStation(target1);
        Line afterLine2 = line.removeStation(target2);
        //then
        assertAll(
                () -> assertThat(afterLine1.getSections()).isEmpty(),
                () -> assertThat(afterLine1.getSections()).isEmpty()
        );
    }

    @DisplayName("모든 역을 순서대로 반환한다.")
    @Test
    void findAllStation() {
        //given
        Section section1 = new Section(new Station("잠실나루"), new Station("잠실"), new Distance(3));
        Section section2 = new Section(new Station("잠실"), new Station("잠실새내"), new Distance(7));
        Line line = new Line (
                new LineName("2호선"),
                new LineColor("초록"),
                section1.getUpStation(),
                section2.getDownStation(),
                List.of(section1, section2)
        );
        //when
        List<Station> stations = line.findAllStation();
        //then
        assertThat(stations).containsOnly(
                        new Station("잠실나루"),
                        new Station("잠실"),
                        new Station("잠실새내"));
    }
}
