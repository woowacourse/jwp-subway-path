package subway.domain.interstation;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.domain.station.Station;
import subway.exception.interstation.InterStationException;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("구간은")
class InterStationTest {

    @Test
    void 정상적으로_생성된다() {
        final Station station1 = new Station(1L, "강남역");
        final Station station2 = new Station(2L, "역삼역");

        assertThatCode(() -> new InterStation(station1, station2, 1L))
            .doesNotThrowAnyException();
    }

    @Test
    void 출발역과_도착역이_같으면_예외가_발생한다() {
        final Station station1 = new Station(1L, "강남역");
        final Station station2 = new Station(1L, "강남역");

        assertThatCode(() -> new InterStation(station1, station2, 1L))
            .isInstanceOf(InterStationException.class)
            .hasMessage("상행역과 하행역이 같습니다.");
    }

    @Test
    void 거리가_음수이면_예외가_발생한다() {
        final Station station1 = new Station(1L, "강남역");
        final Station station2 = new Station(2L, "역삼역");

        assertThatCode(() -> new InterStation(station1, station2, -1L))
            .hasMessage("거리는 양수이어야 합니다.");
    }
}
