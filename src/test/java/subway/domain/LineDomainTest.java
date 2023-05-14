package subway.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineDomainTest {

    @ParameterizedTest
    @ValueSource(strings = {"", "012345678901234567890"})
    void 노선_이름의_길이가_1미만_20초과일_경우_예외가_발생한다(final String nameValue) {
        assertThatThrownBy(() -> new LineDomain(nameValue, "초록", SectionsDomain.from(Collections.emptyList())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 노선_이름이_null일_경우_예외가_발생한다() {
        assertThatThrownBy(() -> new LineDomain(null, "초록", SectionsDomain.from(Collections.emptyList())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 노선_색상이_null일_경우_예외가_발생한다() {
        assertThatThrownBy(() -> new LineDomain("2", null, SectionsDomain.from(Collections.emptyList())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 노선_지하철_목록이_null일_경우_예외가_발생한다() {
        assertThatThrownBy(() -> new LineDomain("2", "초록", null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
