package subway.line.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
class LineTest {
    @ParameterizedTest(name = "{displayName} : name = {0}")
    @NullAndEmptySource
    void 노선_이름이_Null_or_Empty인_경우_예외_발생(final String name) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Line(name, "파랑"));
    }
    
    @ParameterizedTest(name = "{displayName} : color = {0}")
    @NullAndEmptySource
    void 노선_색상이_Null_or_Empty인_경우_예외_발생(final String color) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Line("1호선", color));
    }
}
