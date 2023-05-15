package subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("Station 은(는)")
class StationTest {

    @Test
    void UUID_가_같으면_동등하다() {
        // given
        final Station 잠실역1 = new Station("잠실역");
        final Station 잠실역2 = new Station(잠실역1.id(), "잠실역아님");

        // when & then
        assertThat(잠실역1).isEqualTo(잠실역2);
    }
}
