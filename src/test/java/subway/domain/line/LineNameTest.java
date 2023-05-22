package subway.domain.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.station.StationName;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LineNameTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", "", "   "})
    @DisplayName("노선의 이름은 빈 값이나 null이 될 수 없습니다.")
    void validate_line_name_is_blank_and_null(String name) {
        // when + then
        assertThatThrownBy(() -> new StationName(name))
                .isInstanceOf(IllegalArgumentException.class)
                .describedAs("역 이름은 공백일 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"2호선", "3호선", "4호선", "9호선"})
    @DisplayName("노선의 이름은 한글자 이상으로 생성된다.")
    void generate_name_success(String name) {
        // when + then
        LineName lineName = new LineName(name);

        assertEquals(name, lineName.getName());
    }
}
