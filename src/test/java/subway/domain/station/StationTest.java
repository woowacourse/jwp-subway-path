package subway.domain.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StationTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", "", "   "})
    @DisplayName("역의 이름은 빈 값이나 null이 될 수 없습니다.")
    void validate_station_name_is_blank_and_null(String name) {
        // when + then
        assertThatThrownBy(() -> new StationName(name))
                .isInstanceOf(IllegalArgumentException.class)
                .describedAs("역 이름은 공백일 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"강남역", "선릉역", "잠실역"})
    @DisplayName("이름은 한글자 이상으로 생성된다.")
    void generate_name_success(String name) {
        // when + then
        Station station = new Station(name);

        assertEquals(name, station.getName());
    }
}
