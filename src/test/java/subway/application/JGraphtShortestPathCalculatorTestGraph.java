package subway.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.station.Station;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class JGraphtShortestPathCalculatorTestGraph {

    JGraphtShortestPathCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new JGraphtShortestPathCalculator();
    }

    @Test
    void findPath_메소드는_출발_역과_도착_역이_동일한_경우_예외가_발생한다() {
        final Line line = Line.of(1L, "1호선", "bg-red-500");
        final Station station = Station.of(1L, "1역");

        assertThatThrownBy(() -> calculator.findPath(List.of(line), station, station))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발지와 목적지가 동일할 수 없습니다.");
    }
}
