package subway.domain.station;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StationsTest {

    @Test
    @DisplayName("stations는 1개의 station만 가질 수 없다.")
    void validate_station_count_1() {
        // given
        List<Station> stations = List.of(new Station(1L, "잠실역"));

        // when + then
        assertThatThrownBy(() -> new Stations(stations))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("stations는 중복된 이름의 station을 가질 수 없다.")
    void validate_duplicated_name_station() {
        // given
        List<Station> stations = List.of(new Station(1L, "잠실역"), new Station(2L, "잠실역"));

        // when + then
        assertThatThrownBy(() -> new Stations(stations))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @MethodSource("createStations")
    @DisplayName("stations는 0개 또는 2개 이상의 역으로 생성된다.")
    void generate_stations_success(List<Station> stations) {
        // when + then
        assertDoesNotThrow(() -> new Stations(stations));
    }

    private static Stream<Arguments> createStations() {
        return Stream.of(
            Arguments.arguments(List.of()),
            Arguments.arguments(List.of(new Station(1L, "잠실역"), new Station(2L, "선릉역"))),
            Arguments.arguments(List.of(new Station(1L, "잠실역"), new Station(2L, "선릉역"), new Station(3L, "강남역")))
        );
    }
}
