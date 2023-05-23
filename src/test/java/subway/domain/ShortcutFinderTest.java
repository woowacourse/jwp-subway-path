package subway.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.vo.Section;
import subway.domain.vo.Shortcut;

import java.util.List;

import static subway.fixture.Fixture.*;

class ShortcutFinderTest {
    @Test
    @DisplayName("최소경로를 찾는다")
    void shortcutTest() {
        List<Section> allSections = List.of(SILIM_BONCHUN, BONCHUN_SEOUL, SEOUL_NAKSUNG, NAKSUNG_SADANG, SILIM_NODLE, NODLE_EESU, EESU_SADANG, SILIM_KM_9, SILIM_KM_10, SILIM_KM_11, SILIM_KM_16, SILIM_KM_58);
        ShortcutFinder shortcutFinder = new ShortcutFinder(allSections);
        Shortcut shortcut = shortcutFinder.findShortcut(1l, 5l);
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(shortcut.getPath()).containsExactly(SINLIM, BONGCHUN, SEOUL, NAKSUNG, SADANG);
            softly.assertThat(shortcut.getFee()).isEqualTo(1250);
        });
    }

}
