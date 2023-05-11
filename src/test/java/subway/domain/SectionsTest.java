package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

@SuppressWarnings("NonAsciiCharacters")
class SectionsTest {
    @Test
    void 동일한_역으로_추가하려고_하면_예외() {
        Sections sections = new Sections(new ArrayList<>());
        Station jamsil1 = new Station("잠실");
        Station jamsil2 = new Station("잠실");
        Distance distance = new Distance(10);
        Assertions.assertThatThrownBy(() -> sections.add(jamsil1, jamsil2, distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("동일한 역 2개가 입력으로 들어왔습니다. 이름을 다르게 설정해주세요.");
    }
}
