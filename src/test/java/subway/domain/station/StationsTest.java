package subway.domain.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.DuplicatedStationNameException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StationsTest {

    @Test
    @DisplayName("이미 있는 이름으로 역을 추가할 수 없다.")
    void add_station_with_already_created_name_test() {
        // given
        Station station1 = new Station(1L, "잠실");
        Station station2 = new Station(2L, "잠실");
        final Stations stations = new Stations();
        stations.add(station1);

        // when
        // then
        assertThatThrownBy(() -> stations.add(station2))
                .isInstanceOf(DuplicatedStationNameException.class);
    }

}
