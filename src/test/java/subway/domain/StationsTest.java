package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StationsTest {

    @Test
    @DisplayName("역 이름이 중복되면 예외가 발생한다.")
    void add_fail_test() {
        // given
        final Station 잠실역 = new Station("잠실역");
        final Station 선릉역 = new Station("선릉역");
        final Stations stations = new Stations(Arrays.asList(잠실역, 선릉역));

        // expect
        final Station 중복된_잠실역 = new Station("잠실역");
        assertThatThrownBy(() -> stations.add(중복된_잠실역))
            .isInstanceOf(IllegalArgumentException.class)
            .extracting("message")
            .isEqualTo("역 이름은 중복될 수 없습니다.");
    }

    @Test
    @DisplayName("역을 추가할 수 있다.")
    void add_success_test() {
        // given
        final Station 잠실역 = new Station("잠실역");
        final Stations stations = new Stations(new ArrayList<>());

        // when
        stations.add(잠실역);

        // then
        final List<Station> expectedStations = stations.getStations();
        assertThat(expectedStations.get(0))
            .extracting("name")
            .isEqualTo("잠실역");
    }
}
