package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.service.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;

class StationTest {

    @Test
    @DisplayName("두 Station 이 동일한 이름을 가지고 있는지 확인한다.")
    void compareTwoStationsName() {
        Station ditoo = new Station("ditoo");
        Station matthew = new Station("matthew");
        Station sameNameAsDitoo = new Station("ditoo");

        assertThat(ditoo.hasSameName(matthew)).isFalse();
        assertThat(ditoo.hasSameName(sameNameAsDitoo)).isTrue();
    }

}
