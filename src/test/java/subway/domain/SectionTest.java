package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("Section 은(는)")
class SectionTest {

    @Test
    void 구간_거리가_양수가_아니면_예외() {
        // given
        final Station 출발역 = new Station("출발역");
        final Station 종착역 = new Station("종착역");

        // when & then
        assertThatThrownBy(() ->
                new Section(출발역, 종착역, 0)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
