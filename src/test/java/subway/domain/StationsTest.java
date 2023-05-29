package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.station.Station;
import subway.domain.station.Stations;

import java.util.ArrayList;
import java.util.List;

@DisplayName("Stations는 ")
class StationsTest {

    @Test
    @DisplayName("여러 역에 대한 정보를 갖는다.")
    void stationsCreateTest() {
        // given
        Station station1 = Station.from("잠실");
        Station station2 = Station.from("선릉");

        List<Station> stations = new ArrayList<>(List.of(station1, station2));

        // then
        assertDoesNotThrow(() -> Stations.from(stations));
    }

    @Test
    @DisplayName("새로운 역을 추가할 수 있다.")
    void stationsAddTest() {
        // given
        Station station1 = Station.from("잠실");
        Station station2 = Station.from("선릉");
        Station station3 = Station.from("잠실새내");

        Stations stations = Stations.from(new ArrayList<>(List.of(station1, station2)));

        // then
        assertDoesNotThrow(() -> stations.addStation(station3));
    }


    @Test
    @DisplayName("중복되는 역이 입력되면 예외처리한다.")
    void addDuplicationStationExceptionTest() {
        // given
        Station station1 = Station.from("잠실");
        Station station2 = Station.from("선릉");
        Station station3 = Station.from("잠실");

        Stations stations = Stations.from(new ArrayList<>(List.of(station1, station2)));

        // then
        assertThatThrownBy(() -> stations.addStation(station3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 동일한 이름의 역을 중복으로 등록할 수 없습니다.");
    }

    @ParameterizedTest
    @CsvSource(value = {"잠실,true", "잠실나루,false"})
    @DisplayName("입력된 역이 존재하는 역인지 확인할 수 있다.")
    void containsTest(String stationName, boolean expected) {
        // given
        Station station1 = Station.from("잠실");
        Station station2 = Station.from("선릉");

        Stations stations = Stations.from(new ArrayList<>(List.of(station1, station2)));

        // when
        boolean result = stations.contains(Station.from(stationName));

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("역을 삭제할 수 있다.")
    void removeTest() {
        // given
        Station station1 = Station.from("잠실");
        Station station2 = Station.from("선릉");
        Station station3 = Station.from("잠실");

        Stations stations = Stations.from(new ArrayList<>(List.of(station1, station2)));

        //when
        stations.remove(station3);

        // then
        assertThat(stations.contains(station3)).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 역을 삭제하려고 하면 예외처리한다.")
    void removeInvalidStationExceptionTest() {
        // given
        Station station1 = Station.from("잠실");
        Station station2 = Station.from("선릉");
        Station station3 = Station.from("잠실나루");

        Stations stations = Stations.from(new ArrayList<>(List.of(station1, station2)));

        //then
        assertThatThrownBy(() -> stations.remove(station3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 등록되지 않은 역을 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("등록된 역을 조회할 수 있다.")
    void findTest() {
        // given
        Station station1 = Station.from("잠실");
        Station station2 = Station.from("선릉");
        Station station3 = Station.from("잠실");

        Stations stations = Stations.from(new ArrayList<>(List.of(station1, station2)));

        //when
        Station result = stations.find(station3);

        //then
        assertThat(result).isEqualTo(station1);
    }

    @Test
    @DisplayName("존재하지 않는 역을 조회하면 예외처리 한다.")
    void findInvalidStationExceptionTest() {
        // given
        Station station1 = Station.from("잠실");
        Station station2 = Station.from("선릉");
        Station station3 = Station.from("잠실나루");

        Stations stations = Stations.from(new ArrayList<>(List.of(station1, station2)));

        //then
        assertThatThrownBy(() -> stations.find(station3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 등록되지 않은 역을 조회했습니다.");
    }
}
