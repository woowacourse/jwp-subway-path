package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SectionTest {

    @Test
    void equalsTest() {
        final Section a = new Section(1L, 2L, new Station("선릉역"), new Station("잠실역"), new Distance(3));
        final Section b = new Section(1L, 3L, new Station("낙성대역"), new Station("사당역"), new Distance(4));
        assertThat(a).isEqualTo(b);
    }

    @Test
    void notEqualsTest() {
        final Section a = new Section(1L, 2L, new Station("선릉역"), new Station("잠실역"), new Distance(3));
        final Section b = new Section(2L, 2L, new Station("선릉역"), new Station("잠실역"), new Distance(3));
        assertThat(a).isNotEqualTo(b);
    }
}
