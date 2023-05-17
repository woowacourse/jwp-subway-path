package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.section.Direction;
import subway.domain.section.Distance;
import subway.domain.station.Station;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class LineTest {

    Line line;

    @BeforeEach
    void setUp() {
        line = Line.of(1L, "1호선", "bg-red-500");
    }

    @Test
    void isRegisterStation_메소드는_등록된_station을_전달하면_true를_반환한다() {
        final Station sourceStation = Station.of(1L, "1역");
        final Station targetStation = Station.of(2L, "2역");
        line.createSection(sourceStation, targetStation, Distance.from(5), Direction.DOWN);

        final boolean actual = line.isRegisterStation(sourceStation);

        assertThat(actual).isTrue();
    }

    @Test
    void isRegisterStation_메소드는_등록되지_않은_station을_전달하면_false를_반환한다() {
        final Station station = Station.of(1L, "1역");

        final boolean actual = line.isRegisterStation(station);

        assertThat(actual).isFalse();
    }
}
