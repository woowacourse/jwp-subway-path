package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.service.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;

class StationTest {

    @Test
    @DisplayName("Station 의 equals, hashCode 를 확인한다. (Name 만을 가지고 비교하는지)")
    void equalsAndHashCodeTest() {
        Station originStation = new Station(1, "hello");
        Station sameNameStation = new Station(2, "hello");
        Station otherNameStation = new Station(3, "hell");

        assertThat(originStation.equals(sameNameStation)).isTrue();
        assertThat(originStation.equals(otherNameStation)).isFalse();
        assertThat(originStation.hashCode()).isEqualTo(sameNameStation.hashCode());
        assertThat(originStation.hashCode()).isNotEqualTo(otherNameStation.hashCode());
    }

}
