package subway.domain.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class NameTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", "", "   "})
    @DisplayName("역의 이름은 빈 값이나 null이 될 수 없습니다.")
    void validate_station_name_is_blank_and_null(String name) {
        // when + then
        assertThatThrownBy(() -> new Name(name))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @ParameterizedTest
    @ValueSource(strings = {"강남역", "선릉역", "잠실역"})
    @DisplayName("이름은 한글자 이상으로 생성된다.")
    void generate_name_success(String name) {
        // when + then
        assertDoesNotThrow(() -> new Name(name));
    }
}
