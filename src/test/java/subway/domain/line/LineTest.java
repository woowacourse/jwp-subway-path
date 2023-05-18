package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class LineTest {

    @Test
    void 노선은_이름이_없다면_예외가_발생한다() {
        // given
        final LineColor lineColor = new LineColor("초록");

        // expect
        assertThatThrownBy(() -> new Line(null, lineColor))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선의 이름은 필수 입니다.");
    }

    @Test
    void 노선은_색상이_없다면_예외가_발생한다() {
        // given
        final LineName lineName = new LineName("2호선");

        // expect
        assertThatThrownBy(() -> new Line(lineName, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선의 색상은 필수 입니다.");
    }

    @Test
    void 노선은_직접_없는_구간들로_생성하면_예외가_발생한다() {
        // given
        final LineName lineName = new LineName("2호선");
        final LineColor lineColor = new LineColor("초록");

        // expect
        assertThatThrownBy(() -> new Line(1L, lineName, lineColor, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선의 구간들은 빈 구간이라도 필요합니다.");
    }

    @Test
    void 노선은_구간이_빈_상태로_생성할_수_있다() {
        // given
        final LineName lineName = new LineName("2호선");
        final LineColor lineColor = new LineColor("초록");

        // expect
        assertThat(new Line(lineName, lineColor).isSectionsEmpty()).isTrue();
    }
}
