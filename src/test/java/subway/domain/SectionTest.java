package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionTest {

    private final Station A_STATION = new Station(1L, "A");
    private final Station B_STATION = new Station(2L, "B");

    @DisplayName("station들이 같은 section을 확인하는 기능 테스트")
    @Test
    void isSameStations() {
        final Section section = new Section(A_STATION, B_STATION, new Distance(1));
        final Section otherSection = new Section(A_STATION, B_STATION, new Distance(20));

        assertThat(section.isSameStations(otherSection))
                .isTrue();
    }

    @DisplayName("section을 생성할 때 beforeStation과 nextStation이 같은 경우 예외처리")
    @Test
    void validateSameStation() {
        assertThatThrownBy(() -> new Section(A_STATION, A_STATION, new Distance(10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구간은 서로 다른 두 역이어야 합니다.");
    }
}
