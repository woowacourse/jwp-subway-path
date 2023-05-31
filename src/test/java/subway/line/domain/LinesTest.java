package subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.line.exception.DuplicateLineNameException;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.utils.LineFixture.LINE_NUMBER_TWO;

class LinesTest {

    @Test
    @DisplayName("이름이 겹치지 않으면 정상적으로 Line이 추가된다")
    void addLineSuccess() {
        Lines lines = new Lines(Collections.emptyList());

        assertThatNoException().isThrownBy(
                () -> lines.addLine(LINE_NUMBER_TWO)
        );
    }

    @Test
    @DisplayName("Line의 이름이 겹치면 예외가 발생된다")
    void addLineFail() {
        Lines lines = new Lines(List.of(LINE_NUMBER_TWO));

        assertThatThrownBy(() -> lines.addLine(LINE_NUMBER_TWO))
                .isInstanceOf(DuplicateLineNameException.class);
    }
}
