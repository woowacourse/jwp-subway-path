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
    void 첫_등록은_구간이_비어있어도_등록_가능하다() {
        // given
        final Line line = new Line("8호선", "분홍색", new ArrayList<>());

        // when
        line.register(new Station("잠실역"), new Station("석촌역"), 10);

        // then
        assertThat(line.getSections()).contains(new Section("잠실역", "석촌역", 10));
    }

    @Test
    void 등록시_두_역이_모두_존재하지_않으면_예외가_발생한다() {
        // given
        final Section section = new Section("잠실역", "석촌역", 5);
        final Line line = new Line("8호선", "분홍색", List.of(section));

        // expect
        assertThatThrownBy(() -> line.register(new Station("송파역"), new Station("몽촌토성역"), 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("기준역이 존재하지 않아 추가할 수 없습니다.");
    }

    @Test
    void 등록시_두_역이_모두_존재하면_예외가_발생한다() {
        // given
        final Section section = new Section("잠실역", "석촌역", 3);
        final Line line = new Line("8호선", "분홍색", List.of(section));

        // expect
        assertThatThrownBy(() -> line.register(new Station("잠실역"), new Station("석촌역"), 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("두 역 모두 노선에 존재하는 역입니다");
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 11})
    void 하행_등록시_기준역이_구간의_출발점에_존재할_경우_등록할_구간의_거리가_기존_구간의_거리보다_짧지_않으면_예외가_발생한다(final int distance) {
        // given
        final Section section = new Section("잠실역", "석촌역", 10);
        final Line line = new Line("8호선", "분홍색", List.of(section));

        // expect
        assertThatThrownBy(() -> line.register(new Station("잠실역"), new Station("송파역"), distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록하려는 구간의 거리는 기존 구간의 거리보다 짧아야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 11})
    void 상행_등록시_기준역이_구간의_끝점에_존재할_경우_등록할_구간의_거리가_기존_구간의_거리보다_짧지_않으면_예외가_발생한다(final int distance) {
        // given
        final Section section = new Section("잠실역", "석촌역", 10);
        final Line line = new Line("8호선", "분홍색", List.of(section));

        // expect
        assertThatThrownBy(() -> line.register(new Station("송파역"), new Station("잠실역"), distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록하려는 구간의 거리는 기존 구간의 거리보다 짧아야 합니다.");
    }

    @Test
    void 이미_존재하는_구간_사이에_역을_등록하는_경우_거리_정보가_갱신된다() {
        // given
        final Section section1 = new Section("잠실역", "석촌역", 10);
        final Section section2 = new Section("석촌역", "송파역", 10);
        final Line line = new Line("8호선", "분홍색", List.of(section1, section2));

        // when
        line.register(new Station("잠실역"), new Station("가락시장역"), 5);

        // then
        assertThat(line.getSections()).containsAll(List.of(
                new Section("잠실역", "가락시장역", 5),
                new Section("가락시장역", "석촌역", 5),
                new Section("석촌역", "송파역", 10)
        ));
    }

    @Test
    void 상행_종점을_등록한다() {
        // given
        final Section section = new Section("잠실역", "석촌역", 10);
        final Line line = new Line("8호선", "분홍색", List.of(section));

        // when
        line.register(new Station("몽촌토성역"), new Station("잠실역"), 5);

        // then
        assertThat(line.getSections()).containsAll(List.of(
                new Section("몽촌토성역", "잠실역", 5),
                new Section("잠실역", "석촌역", 10)
        ));
    }

    @Test
    void 하행_종점을_등록한다() {
        // given
        final Section section = new Section("잠실역", "석촌역", 10);
        final Line line = new Line("8호선", "분홍색", List.of(section));

        // when
        line.register(new Station("석촌역"), new Station("송파역"), 5);

        // then
        assertThat(line.getSections()).containsAll(List.of(
                new Section("잠실역", "석촌역", 10),
                new Section("석촌역", "송파역", 5)
        ));
    }
}
