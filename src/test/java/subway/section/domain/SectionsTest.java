package subway.section.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
class SectionsTest {
    @Test
    void 최초_역_등록_시_두_역을_동시에_등록한다() {
        // given
        final Sections sections = new Sections();
        final String left = "잠실역";
        final String right = "선릉역";
        final long distance = 3L;
        
        // when
        sections.initAddStation(left, right, distance);
        
        // then
        assertThat(sections.getSections()).contains(new Section(left, right, distance));
    }
}
