package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LineTest {

    private static final Station STATION_A = new Station(1L, "a");
    private static final Station STATION_B = new Station(2L, "b");
    private static final Station STATION_C = new Station(3L, "c");
    private static final Station STATION_D = new Station(4L, "d");

    @DisplayName("Line에 Section을 추가한다.")
    @Test
    void addSection() {
        final Line line = new Line(1L, new LineName("2호선"));
        final Section section1 = new Section(STATION_A, STATION_B, new Distance(7));
        final Section section2 = new Section(STATION_B, STATION_C, new Distance(10));
        final Section newSection = new Section(STATION_B, STATION_D, new Distance(3));

        final Line newLine = line.addSection(section1)
                .addSection(section2)
                .addSection(newSection);

        final List<Section> sections = newLine.getSections().getSections();
        assertAll(
                () -> assertThat(sections)
                        .extracting(Section::getBeforeStation)
                        .extracting(Station::getName)
                        .containsExactly("a", "b", "d"),
                () -> assertThat(sections)
                        .extracting(Section::getNextStation)
                        .extracting(Station::getName)
                        .containsExactly("b", "d", "c"),
                () -> assertThat(sections)
                        .extracting(Section::getDistance)
                        .extracting(Distance::getValue)
                        .containsExactly(7, 3, 7)
        );
    }

    @Nested
    @DisplayName("Line에 Station을 제거한다.")
    class removeStation {

        @DisplayName("상행 종점을 제거하는 경우")
        @Test
        void removeHeadStation() {
            final Line line = new Line(1L, new LineName("2호선"));
            final Section section1 = new Section(STATION_A, STATION_B, new Distance(7));
            final Section section2 = new Section(STATION_B, STATION_C, new Distance(10));
            final Line newLine = line.addSection(section1)
                    .addSection(section2);


            final Line removedLine = newLine.removeStation(STATION_A);
            final List<Section> sections = removedLine.getSections().getSections();
            assertAll(
                    () -> assertThat(sections)
                            .extracting(Section::getBeforeStation)
                            .extracting(Station::getName)
                            .containsExactly("b"),
                    () -> assertThat(sections)
                            .extracting(Section::getNextStation)
                            .extracting(Station::getName)
                            .containsExactly("c"),
                    () -> assertThat(sections)
                            .extracting(Section::getDistance)
                            .extracting(Distance::getValue)
                            .containsExactly(10)
            );
        }

        @DisplayName("하행 종점을 제거하는 경우")
        @Test
        void removeTailStation() {
            final Line line = new Line(1L, new LineName("2호선"));
            final Section section1 = new Section(STATION_A, STATION_B, new Distance(7));
            final Section section2 = new Section(STATION_B, STATION_C, new Distance(10));
            final Line newLine = line.addSection(section1)
                    .addSection(section2);


            final Line removedLine = newLine.removeStation(STATION_C);
            final List<Section> sections = removedLine.getSections().getSections();

            assertAll(
                    () -> assertThat(sections)
                            .extracting(Section::getBeforeStation)
                            .extracting(Station::getName)
                            .containsExactly("a"),
                    () -> assertThat(sections)
                            .extracting(Section::getNextStation)
                            .extracting(Station::getName)
                            .containsExactly("b"),
                    () -> assertThat(sections)
                            .extracting(Section::getDistance)
                            .extracting(Distance::getValue)
                            .containsExactly(7)
            );
        }

        @DisplayName("중간 역을 제거하는 경우")
        @Test
        void removeCentralStation() {
            final Line line = new Line(1L, new LineName("2호선"));
            final Section section1 = new Section(STATION_A, STATION_B, new Distance(7));
            final Section section2 = new Section(STATION_B, STATION_C, new Distance(10));
            final Line newLine = line.addSection(section1)
                    .addSection(section2);


            final Line removedLine = newLine.removeStation(STATION_B);
            final List<Section> sections = removedLine.getSections().getSections();

            assertAll(
                    () -> assertThat(sections)
                            .extracting(Section::getBeforeStation)
                            .extracting(Station::getName)
                            .containsExactly("a"),
                    () -> assertThat(sections)
                            .extracting(Section::getNextStation)
                            .extracting(Station::getName)
                            .containsExactly("c"),
                    () -> assertThat(sections)
                            .extracting(Section::getDistance)
                            .extracting(Distance::getValue)
                            .containsExactly(17)
            );
        }

        @DisplayName("역이 두개만 있을 때 하나를 제거하는 경우")
        @Test
        void removeWhenLineHas2Station() {
            final Line line = new Line(1L, new LineName("2호선"));
            final Section section1 = new Section(STATION_A, STATION_B, new Distance(7));
            final Line newLine = line.addSection(section1);

            final Line removedLine = newLine.removeStation(STATION_B);
            final List<Section> sections = removedLine.getSections().getSections();

            assertThat(sections.isEmpty()).isTrue();
        }
    }
}
