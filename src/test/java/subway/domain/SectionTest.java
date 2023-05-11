package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SectionTest {

    private final Station A_STATION = new Station(1L, "A");
    private final Station B_STATION = new Station(2L, "B");

    @Test
    void isSameStations() {
        final Section section = new Section(A_STATION, B_STATION, new Distance(1));
        final Section otherSection = new Section(A_STATION, B_STATION, new Distance(20));

        assertThat(section.isSameStations(otherSection))
                .isTrue();
    }
}
