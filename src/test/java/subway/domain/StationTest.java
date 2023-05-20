package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.controller.exception.InvalidStationException;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StationTest {

    @ParameterizedTest(name = "세글자 이상 열글자 초과의 이름은 정상 생성된다")
    @ValueSource(strings = {"일이삼", "일이삼사오륙칠팔구십"})
    void 세글자부터_열글자_사이의_이름은_정상_생성된다(final String name) {
        // expect
        assertDoesNotThrow(() -> new Station(name));
    }

    @ParameterizedTest(name = "세글자 미만 열글자 초과의 이름은 예외가 발생한다.")
    @ValueSource(strings = {"일이", "영일이삼사오륙칠팔구십"})
    void 세글자_미만_열글자_초과의_이름은_예외가_발생한다(final String name) {
        // expect
        assertThatThrownBy(() -> new Station(name))
                .isInstanceOf(InvalidStationException.class)
                .hasMessageContaining("역 이름은 3~10자 사이여야 합니다");
    }

    @Test
    void 이름이_같으면_동등하다() {
        // given
        final String name = "잠실역";

        // when
        final Station firstStation = new Station(name);
        final Station secondStation = new Station(name);

        // then
        assertThat(firstStation).isEqualTo(secondStation);
    }
}
