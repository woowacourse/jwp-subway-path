package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineTest {

    @ParameterizedTest(name = "세글자 이상 열글자 초과의 이름은 정상 생성된다")
    @ValueSource(strings = {"일이삼", "일이삼사오륙칠팔구십"})
    void 세글자부터_열글자_사이의_이름은_정상_생성된다(final String name) {
        // expect
        assertDoesNotThrow(() -> new Line(name, "초록색", new ArrayList<>()));
    }

    @ParameterizedTest(name = "세글자 미만 열글자 초과의 이름은 예외가 발생한다.")
    @ValueSource(strings = {"일이", "영일이삼사오륙칠팔구십"})
    void 세글자_미만_열글자_초과의_이름은_예외가_발생한다(final String name) {
        // expect
        assertThatThrownBy(() -> new Line(name, "분홍색", new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("노선 이름은 3~10자 사이여야 합니다");
    }

    @Test
    void 구간을_등록할_수_있다() {
        // given
        final Line line = new Line("8호선", "분홍색", new ArrayList<>());

        // when
        line.register(new Station("잠실역"), new Station("석촌역"), 10);

        // then
        assertThat(line.sections()).contains(new Section("잠실역", "석촌역", 10));
    }

    @Test
    void 역을_제거할_수_있다() {
        // given
        final Section firstSection = new Section("잠실역", "석촌역", 10);
        final Section secondSection = new Section("석촌역", "송파역", 10);
        final Line line = new Line("8호선", "분홍색", List.of(firstSection, secondSection));

        // when
        line.delete(new Station("석촌역"));

        // then
        assertThat(line.sections()).contains(new Section("잠실역", "송파역", 20));
    }
}
