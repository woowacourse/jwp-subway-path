package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.domain.section.Sections.emptySections;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class SectionsTest {

    @Test
    void 구간들은_null_일_시_예외가_발생한다() {
        assertThatThrownBy(() -> new Sections(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선에 구간들은 없을 수 없습니다.");
    }

    @Test
    void 구간들은_비어있는_상태로_생성될_수_있다() {
        assertThat(emptySections().getSections()).isEmpty();
    }
}
