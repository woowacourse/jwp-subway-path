package subway.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.Test;

class StationTest {

    @Test
    void ID와_이름이_같으면_같은_객체이다() {
        // given
        Station station1 = new Station(1L, "강남");
        Station station2 = new Station(1L, "강남");

        // expect
        assertThat(station1).isEqualTo(station2);
    }
}
