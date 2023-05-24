package subway.domain.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StationsTest {

    @Test
    @DisplayName("stations는 1개의 station만 가질 수 없다.")
    void validate_station_count_1() {
        // given
        List<Station> stations = List.of(new Station(1L, "잠실역"));

        // when + then
        assertThatThrownBy(() -> new Stations(stations))
                .isInstanceOf(IllegalArgumentException.class)
                .describedAs("노선의 역은 1개만 존재할 수 없습니다.");
    }

    @Test
    @DisplayName("stations는 중복된 이름의 station을 가질 수 없다.")
    void validate_duplicated_name_station() {
        // given
        List<Station> stations = List.of(new Station(1L, "잠실역"), new Station(2L, "잠실역"));

        // when + then
        assertThatThrownBy(() -> new Stations(stations))
                .isInstanceOf(IllegalArgumentException.class)
                .describedAs("역 이름은 중복될 수 없습니다.");
    }

    @ParameterizedTest
    @MethodSource("createStations")
    @DisplayName("stations는 0개 또는 2개 이상의 역으로 생성된다.")
    void generate_stations_success(List<Station> stations) {
        // when + then
        Stations stationsInstance = new Stations(stations);

        assertEquals(stations.size(), stationsInstance.size());
    }

    private static Stream<Arguments> createStations() {
        return Stream.of(
                Arguments.arguments(List.of()),
                Arguments.arguments(List.of(new Station(1L, "잠실역"), new Station(2L, "선릉역"))),
                Arguments.arguments(List.of(new Station(1L, "잠실역"), new Station(2L, "선릉역"), new Station(3L, "강남역")))
        );
    }
}
