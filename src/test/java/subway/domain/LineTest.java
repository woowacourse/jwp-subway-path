package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static subway.TestFixture.LINE_2;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.section.Section;
import subway.exception.AlreadyHasStationsException;
import subway.exception.StationNotFoundInSectionsException;

class LineTest {

    private static final Station STATION_A = new Station(1L, "a");
    private static final Station STATION_B = new Station(2L, "b");
    private static final Station STATION_C = new Station(3L, "c");
    private static final Station STATION_D = new Station(4L, "d");
    private static final Section SECTION_1 = new Section(STATION_A, STATION_B, new Distance(7));
    private static final Section SECTION_2 = new Section(STATION_B, STATION_C, new Distance(10));
    private static final Line LINE = new Line(LINE_2.getId(), LINE_2.getName());

    @DisplayName("Line에 Section을 추가한다.")
    @Test
    void addSection() {
        final Section newSection = new Section(STATION_B, STATION_D, new Distance(3));

        final Line newLine = LINE.addSection(SECTION_2)
                .addSection(SECTION_1)
                .addSection(newSection);

        final List<Section> sections = newLine.getSections().getSections();

        assertThat(sections)
                .extracting(Section::getPrevStation, Section::getNextStation, Section::getDistance)
                .containsExactly(
                        tuple(STATION_A, STATION_B, new Distance(7)),
                        tuple(STATION_B, STATION_D, new Distance(3)),
                        tuple(STATION_D, STATION_C, new Distance(7))
                );
    }

    @Nested
    @DisplayName("Line에 Station을 제거한다.")
    class removeStation {

        @DisplayName("상행 종점을 제거하는 경우")
        @Test
        void removeHeadStation() {
            final Line newLine = LINE.addSection(SECTION_1)
                    .addSection(SECTION_2);

            final Line removedLine = newLine.removeStation(STATION_A);
            final List<Section> sections = removedLine.getSections().getSections();

            assertThat(sections)
                    .extracting(Section::getPrevStation, Section::getNextStation, Section::getDistance)
                    .containsExactly(
                            tuple(STATION_B, STATION_C, new Distance(10))
                    );
        }

        @DisplayName("하행 종점을 제거하는 경우")
        @Test
        void removeTailStation() {
            final Line newLine = LINE.addSection(SECTION_1)
                    .addSection(SECTION_2);

            final Line removedLine = newLine.removeStation(STATION_C);
            final List<Section> sections = removedLine.getSections().getSections();

            assertThat(sections)
                    .extracting(Section::getPrevStation, Section::getNextStation, Section::getDistance)
                    .containsExactly(
                            tuple(STATION_A, STATION_B, new Distance(7))
                    );
        }

        @DisplayName("중간 역을 제거하는 경우")
        @Test
        void removeCentralStation() {
            final Line newLine = LINE.addSection(SECTION_1)
                    .addSection(SECTION_2);

            final Line removedLine = newLine.removeStation(STATION_B);
            final List<Section> sections = removedLine.getSections().getSections();

            assertThat(sections)
                    .extracting(Section::getPrevStation, Section::getNextStation, Section::getDistance)
                    .containsExactly(
                            tuple(STATION_A, STATION_C, new Distance(17))
                    );
        }

        @DisplayName("역이 두개만 있을 때 하나를 제거하는 경우")
        @Test
        void removeWhenLineHas2Station() {
            final Line newLine = LINE.addSection(SECTION_1);

            final Line removedLine = newLine.removeStation(STATION_B);
            final List<Section> sections = removedLine.getSections().getSections();

            assertThat(sections.isEmpty()).isTrue();
        }

        @DisplayName("존재하지 않는 역을 제거하려는 경우")
        @Test
        void removeNotExistStation() {
            final Line line = LINE.addSection(SECTION_1)
                    .addSection(SECTION_2);

            assertThatThrownBy(() -> line.removeStation(STATION_D))
                    .isInstanceOf(StationNotFoundInSectionsException.class);
        }
    }

    @DisplayName("이미 등록된 구간이 있는 경우")
    @Test
    void validateDuplicateSection() {
        final Line newLine = LINE.addSection(SECTION_1);

        assertThatThrownBy(() -> newLine.addSection(SECTION_1))
                .isInstanceOf(AlreadyHasStationsException.class);
    }
}
