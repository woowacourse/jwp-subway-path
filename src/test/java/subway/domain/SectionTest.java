package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.section.Section;
import subway.exception.SectionHasSameStationsException;

class SectionTest {

    private final Station A_STATION = new Station(1L, "A");
    private final Station B_STATION = new Station(2L, "B");

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
}
