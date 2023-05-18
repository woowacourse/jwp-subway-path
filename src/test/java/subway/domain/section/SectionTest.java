package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.Distance;
import subway.domain.Station;
import subway.exception.CanNotSplitSectionByNextStationException;
import subway.exception.CanNotSplitSectionByPrevStationException;
import subway.exception.SectionHasSameStationsException;
import subway.exception.SplitSectionIsSmallerThanSplitterException;

class SectionTest {

    private final Station A_STATION = new Station(1L, "A");
    private final Station B_STATION = new Station(2L, "B");
    private final Station C_STATION = new Station(3L, "C");

    @DisplayName("Station이 해당 Section에 포함되어있는지 확인한다.")
    @Test
    void containStation() {
        final Section section = new Section(A_STATION, B_STATION, new Distance(10));

        assertAll(
                () -> assertThat(section.containStation(A_STATION)).isTrue(),
                () -> assertThat(section.containStation(B_STATION)).isTrue(),
                () -> assertThat(section.containStation(new Station(3L, "C"))).isFalse()
        );
    }

    @DisplayName("section을 생성할 때 prevStation과 nextStation이 같은 경우 예외처리")
    @Test
    void validateSameStation() {
        assertThatThrownBy(() -> new Section(A_STATION, A_STATION, new Distance(10)))
                .isInstanceOf(SectionHasSameStationsException.class);
    }

    @DisplayName("section의 prevStation이 같은지 확인한다.")
    @Test
    void isEqualPrevStation() {
        final Section section = new Section(A_STATION, B_STATION, new Distance(10));

        assertAll(
                () -> assertThat(section.isEqualPrevStation(A_STATION)).isTrue(),
                () -> assertThat(section.isEqualPrevStation(B_STATION)).isFalse()
        );
    }

    @DisplayName("section의 nextStation이 같은지 확인한다.")
    @Test
    void isEqualNextStation() {
        final Section section = new Section(A_STATION, B_STATION, new Distance(10));

        assertAll(
                () -> assertThat(section.isEqualNextStation(B_STATION)).isTrue(),
                () -> assertThat(section.isEqualNextStation(A_STATION)).isFalse()
        );
    }

    @DisplayName("이전역 기준으로 Section을 분리하는 기능 테스트")
    @Nested
    class splitByPrev {

        @DisplayName("정상적으로 동작할 때 분리한 구간의 List를 반환한다.")
        @Test
        void success() {
            final Section splitSection = new Section(A_STATION, B_STATION, new Distance(10));
            final Section splitterSection = new Section(A_STATION, C_STATION, new Distance(4));

            final List<Section> result = splitSection.splitByPrev(splitterSection);

            assertThat(result)
                    .extracting(Section::getPrevStation, Section::getNextStation, Section::getDistance)
                    .containsExactly(
                            tuple(A_STATION, C_STATION, new Distance(4)),
                            tuple(C_STATION, B_STATION, new Distance(6))
                    );
        }

        @DisplayName("이전역이 동일하지 않은 경우 예외처리")
        @Test
        void prevStationNotSame() {
            final Section splitSection = new Section(A_STATION, B_STATION, new Distance(10));
            final Section splitterSection = new Section(B_STATION, C_STATION, new Distance(4));

            assertThatThrownBy(() -> splitSection.splitByPrev(splitterSection))
                    .isInstanceOf(CanNotSplitSectionByPrevStationException.class);
        }

        @DisplayName("자르려는 구간의 길이가, 요청 구간의 길이보다 짧거나 같을 때 예외처리")
        @Test
        void spitedSectionIsSmallerThanSplitter() {
            final Section splitSection = new Section(A_STATION, B_STATION, new Distance(10));
            final Section splitterSection = new Section(A_STATION, C_STATION, new Distance(11));

            assertThatThrownBy(() -> splitSection.splitByPrev(splitterSection))
                    .isInstanceOf(SplitSectionIsSmallerThanSplitterException.class);
        }
    }

    @DisplayName("다음역 기준으로 Section을 분리하는 기능 테스트")
    @Nested
    class splitByNext {

        @DisplayName("정상적으로 동작할 때 분리한 구간의 List를 반환한다.")
        @Test
        void success() {
            final Section splitSection = new Section(A_STATION, B_STATION, new Distance(10));
            final Section splitterSection = new Section(C_STATION, B_STATION, new Distance(4));

            final List<Section> result = splitSection.splitByNext(splitterSection);

            assertThat(result)
                    .extracting(Section::getPrevStation, Section::getNextStation, Section::getDistance)
                    .containsExactly(
                            tuple(A_STATION, C_STATION, new Distance(6)),
                            tuple(C_STATION, B_STATION, new Distance(4))
                    );
        }

        @DisplayName("다음역이 동일하지 않은 경우 예외처리")
        @Test
        void prevStationNotSame() {
            final Section splitSection = new Section(A_STATION, B_STATION, new Distance(10));
            final Section splitterSection = new Section(B_STATION, C_STATION, new Distance(4));

            assertThatThrownBy(() -> splitSection.splitByNext(splitterSection))
                    .isInstanceOf(CanNotSplitSectionByNextStationException.class);
        }

        @DisplayName("자르려는 구간의 길이가, 요청 구간의 길이보다 짧거나 같을 때 예외처리")
        @Test
        void spitedSectionIsSmallerThanSplitter() {
            final Section splitSection = new Section(A_STATION, B_STATION, new Distance(10));
            final Section splitterSection = new Section(C_STATION, B_STATION, new Distance(11));

            assertThatThrownBy(() -> splitSection.splitByNext(splitterSection))
                    .isInstanceOf(SplitSectionIsSmallerThanSplitterException.class);
        }
    }
}
