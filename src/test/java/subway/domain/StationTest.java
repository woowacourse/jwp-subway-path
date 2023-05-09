package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.BusinessException;

class StationTest {

    @NullAndEmptySource
    @ParameterizedTest
    @ValueSource(strings = {" "})
    public void 역이름은_공백을_허용하지_않는다(final String value) {
        //given
        //when
        //then
        assertThatThrownBy(() -> new Station(value))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    public void 역이름은_20글자를_넘지_않는다() {
        //given
        final String value = "t".repeat(21);

        //when
        //then
        assertThatThrownBy(() -> new Station(value))
            .isInstanceOf(BusinessException.class);
    }
}
