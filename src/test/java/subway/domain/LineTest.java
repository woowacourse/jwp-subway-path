package subway.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.NameLengthException;
import subway.utils.SectionFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static subway.utils.SectionFixture.JAMSIL_TO_JAMSILNARU;

class LineTest {

    @ParameterizedTest
    @ValueSource(strings = {"가", "서울대입구서울대16자이름입니다"})
    void 이름이_2이상_15이하가_아니면_예외를_던진다(String invalidLineName) {
        assertThatThrownBy(() -> new Line(invalidLineName, List.of(JAMSIL_TO_JAMSILNARU)))
                .isInstanceOf(NameLengthException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"잠실", "서울대입구서울15자이름입니다"})
    void 이름이_2이상_15이하인_경우_정상_생성된다(String validLineName) {
        assertThatNoException().isThrownBy(() -> new Line(validLineName, List.of(JAMSIL_TO_JAMSILNARU)));
    }
}