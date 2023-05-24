package subway.domain.path;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.section.Distance;
import subway.domain.station.Station;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class PathSectionTest {

    @Test
    void of_메소드는_동일한_역을_전달하면_예외가_발생한다() {
        final Station station = Station.of(1L, "1역");
        final Distance distance = Distance.from(1);

        assertThatThrownBy(() -> PathSection.of(station, station, distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("동일한 역은 이동할 수 없습니다.");
    }
}
