package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SectionTest {

    @Test
    void 출발지와_도착지가_같으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Section("A", "A", 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발지와 도착지는 같을 수 없습니다");
        ;
    }
}
