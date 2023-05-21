package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LineTest {

    @ParameterizedTest
    @CsvSource(value = {"2호선:True", "-100호선:False"}, delimiter = ':')
    @DisplayName("노선 이름이 같으면 True를 반환하고, 다르면 False를 반환한다.")
    void isSameName(String lineName, boolean expected) {
        // given
        Line line = new Line(1L, "2호선");

        // when, then
        assertThat(line.isSameName(lineName)).isEqualTo(expected);
    }
}
