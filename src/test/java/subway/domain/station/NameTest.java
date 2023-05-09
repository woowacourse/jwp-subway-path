package subway.domain.station;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.regex.Pattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.InvalidStationNameException;

class NameTest {

    @Test
    @DisplayName("역 이름이 11글자 이상일 경우 예외를 던진다.")
    void validate_with_invalid_length() {
        assertThatThrownBy(() -> new Name("열한글자가넘는역이름입니다역"))
                .isInstanceOf(InvalidStationNameException.class)
                .hasMessage("역 이름은 1글자에서 11글자까지 가능합니다.");
    }

    @Test
    @DisplayName("역 이름이 역으로 끝나지 않을 경우 예외를 던진다.")
    void validate_with_invalid_name_format() {
        assertThatThrownBy(() -> new Name("선릉"))
                .isInstanceOf(InvalidStationNameException.class)
                .hasMessage("역 이름은 '역'으로 끝나야 합니다.");
    }

    @Test
    @DisplayName("역 이름이 한글, 숫자로 구성되지 않을 경우 예외를 던진다.")
    void validate_with_invalid_name_element() {
        assertThatThrownBy(() -> new Name("NewYork역"))
                .isInstanceOf(InvalidStationNameException.class)
                .hasMessage("역 이름은 한글, 숫자만 가능합니다.");
    }
}

class Name {

    private static final int MAXIMUM_LENGTH = 11;
    private static final String SUFFIX = "역";
    private static final Pattern PATTERN = Pattern.compile("^[가-힣0-9]+$");

    private final String value;

    public Name(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (value.isBlank() || value.length() > MAXIMUM_LENGTH) {
            throw new InvalidStationNameException("역 이름은 1글자에서 11글자까지 가능합니다.");
        }
        if (!value.endsWith(SUFFIX)) {
            throw new InvalidStationNameException("역 이름은 '역'으로 끝나야 합니다.");
        }
        if (!PATTERN.matcher(value).matches()) {
            throw new InvalidStationNameException("역 이름은 한글, 숫자만 가능합니다.");
        }
    }
}
