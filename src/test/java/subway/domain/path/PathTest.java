package subway.domain.path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.Station;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathTest {

    @DisplayName("거리가 0 이하이면 예외가 발생한다")
    @Test
    void notPositiveDistance() {
        //given
        final Station station = new Station("서면역");

        //when, then
        assertThatThrownBy(() -> new Path(station, station, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
