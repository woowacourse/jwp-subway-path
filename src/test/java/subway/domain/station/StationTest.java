package subway.domain.station;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StationTest {

    @Test
    @DisplayName("빈 이름을 가진 지하철 객체를 반환한다.")
    void empty() {
        assertThat(Station.empty().name())
            .isEqualTo(StationName.empty());
    }
}
