package subway.domain.station;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class StationTest {

    @Test
    void equalsTest() {
        final Station a = new Station("잠실역");
        final Station b = new Station("선릉역");
        assertThat(a).isEqualTo(b);
    }

    @Test
    void notEqualsTest() {
        final Station a = new Station(1L, "잠실역");
        final Station b = new Station(null, "잠실역");
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void nullIdEqualsTest() {
        final Station a = new Station("잠실역");
        final Station b = new Station("선릉역");
        assertThat(a).isEqualTo(b);
    }
}
