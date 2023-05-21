package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.station.Station;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StationTest {

    @Test
    @DisplayName("isSame() : 이름이 같으면 같은 역이다.")
    void test_isSame() throws Exception {
        //given
        final Station station1 = new Station("A");
        final Station station2 = new Station("A");

        //when & then
        assertTrue(station1.isSame(station2));
    }
}
