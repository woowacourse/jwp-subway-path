package subway.station.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.vo.Name;

import java.util.HashMap;
import java.util.Map;

class StationTest {

    Station station;

    @BeforeEach
    void setUp() {
        this.station = Station.of(1L, Name.from("잠실역"));
    }

    @DisplayName("해당 역이 종점역인지 아닌지 판단해서 반환한다.(종점역인 경우)")
    @Test
    void isFinalStations() {
        // given
        final Map<String, Station> finalStations = new HashMap<>();
        finalStations.put("finalUpStation", Station.of(1L, Name.from("잠실역")));
        finalStations.put("finalDownStation", Station.of(2L, Name.from("하행종점역")));

        // when
        boolean result = station.isFinalStations(finalStations);

        // then
        Assertions.assertThat(result).isFalse();
    }

    @DisplayName("해당 역이 종점역인지 아닌지 판단해서 반환한다.(종점역이 아닌 경우)")
    @Test
    void isNotFinalStations() {
        // given
        final Map<String, Station> finalStations = new HashMap<>();
        finalStations.put("finalUpStation", Station.of(2L, Name.from("상행종점역")));
        finalStations.put("finalDownStation", Station.of(3L, Name.from("하행종점역")));

        // when
        boolean result = station.isFinalStations(finalStations);

        // then
        Assertions.assertThat(result).isFalse();
    }
}
