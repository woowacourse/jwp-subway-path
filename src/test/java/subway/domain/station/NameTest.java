package subway.domain.station;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.InvalidStationNameException;

class NameTest {

    @Test
    @DisplayName("역 이름을 정상적으로 생성한다.")
    void name() {
        assertDoesNotThrow(() -> new Name("을지로3가역"));
    }

    @Test
    @DisplayName("역 이름 최대 글자를 넘으면 예외를 던진다.")
    void validateWithInvalidLength() {
        final String input = "열한글자가넘는역이름입니다역";

        assertThatThrownBy(() -> new Name(input))
                .isInstanceOf(InvalidStationNameException.class)
                .hasMessage("역 이름은 11글자까지 가능합니다.");
    }

    @Test
    @DisplayName("역 이름이 역으로 끝나지 않을 경우 예외를 던진다.")
    void validateWithInvalidNameFormat() {
        assertThatThrownBy(() -> new Name("선릉"))
                .isInstanceOf(InvalidStationNameException.class)
                .hasMessage("역 이름은 한글, 숫자만 가능하고, '역'으로 끝나야 합니다.");
    }

    @ParameterizedTest
    @DisplayName("역 이름이 한글, 숫자로 구성되지 않을 경우 예외를 던진다.")
    @ValueSource(strings = {"역", "NewYork역", "!!역"})
    void validateWithInvalidNameElement(final String input) {
        assertThatThrownBy(() -> new Name(input))
                .isInstanceOf(InvalidStationNameException.class)
                .hasMessage("역 이름은 한글, 숫자만 가능하고, '역'으로 끝나야 합니다.");
    }

    @ParameterizedTest
    @DisplayName("이름이 빈 칸이거나 null 일 때 예외를 던진다.")
    @NullAndEmptySource
    void validateWithBlankName(final String input) {
        assertThatThrownBy(() -> new Name(input))
                .isInstanceOf(InvalidStationNameException.class)
                .hasMessage("역 이름은 공백일 수 없습니다.");
    }
}
