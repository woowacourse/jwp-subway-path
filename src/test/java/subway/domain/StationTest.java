package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.station.Station;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class StationTest {

    @Test
    void 같은_역이라면_동등하다() {
        final Station station1 = new Station("잠실역");
        final Station station2 = new Station("잠실역");

        assertThat(station1).isEqualTo(station2);
    }
}
