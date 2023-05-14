package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SectionTest {

    @Test
    void 출발역과_도착역이_동일한_경우_예외가_발생한다() {
        // given
        final String stationName = "잠실역";

        // expect
        assertThatThrownBy(() -> new Section(stationName, stationName, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역과 도착역은 동일할 수 없습니다.");
    }

    @Test
    void 거리_정보가_1보다_작은_경우_예외가_발생한다() {
        // given
        final int distance = 0;

        // expect
        assertThatThrownBy(() -> new Section("잠실역", "석촌역", distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("거리 정보는 1 이상이어야 합니다.");
    }

    @Test
    void 출발역과_도착역이_다르며_거리_정보가_1이상이면_생성된다() {
        // given
        final String source = "잠실역";
        final String target = "석촌역";
        final int distance = 1;

        // when
        final Section section = new Section(source, target, distance);

        // expect
        assertAll(
                () -> assertThat(section.getSource()).isEqualTo(new Station("잠실역")),
                () -> assertThat(section.getTarget()).isEqualTo(new Station("석촌역")),
                () -> assertThat(section.getDistance()).isEqualTo(1)
        );
    }
}
