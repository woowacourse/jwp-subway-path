package subway.domain.station;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.InvalidStationNameException;

class NameTest {

    @Test
    @DisplayName("역 이름이 11글자 이상일 경우 예외를 던진다.")
    void validate_with_invalid_length() {
        assertThatThrownBy(() -> new Name("열한글자가넘는역이름입니다역"))
                .isInstanceOf(InvalidStationNameException.class)
                .hasMessage("역 이름은 11글자를 넘을 수 없습니다.");
    }
}

class Name {

    private final String value;

    public Name(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (value.length() > 11) {
            throw new InvalidStationNameException("역 이름은 11글자를 넘을 수 없습니다.");
        }
    }
}
