package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SubwayTest {

    @Test
    void 모든_노선을_조회한다() {
        // given
        final Section firstSection = new Section("잠실역", "석촌역", 10);
        final Section secondSection = new Section("석촌역", "송파역", 10);
        final Section thirdSection = new Section("모란역", "야탑역", 10);
        final Section fourthSection = new Section("야탑역", "이매역", 10);

        final Line firstLine = new Line("8호선", "분홍색", List.of(firstSection, secondSection));
        final Line secondLine = new Line("분당선", "노란색", List.of(thirdSection, fourthSection));

        final Subway subway = new Subway(List.of(firstLine, secondLine));

        // when
        final List<Line> lines = subway.getLines();

        // then
        assertThat(lines).containsExactly(firstLine, secondLine);
    }

    @Test
    void 노선을_등록한다() {
        // given
        final Subway subway = new Subway();

        // when
        subway.registerLine(new Line("8호선", "분홍색"));

        // then
        assertThat(subway.getLines()).contains(new Line("8호선", "분홍색"));
    }

    @Test
    void 노선과_구간으로_등록한다() {
        // given
        final Section section = new Section("잠실역", "석촌역", 10);
        final Line line = new Line("8호선", "분홍색", List.of(section));

        final Subway subway = new Subway(List.of(line));

        // when
        subway.registerSection("8호선", new Station("석촌역"), new Station("송파역"), 8);

        // then
        assertThat(subway.getLines()).contains(new Line("8호선", "분홍색"));
    }


    @Test
    void 중복된_이름의_노선을_등록하면_예외가_발생한다() {
        // given
        final String name = "8호선";

        final Line line = new Line(name, "분홍색");
        final Subway subway = new Subway(List.of(line));

        // expect
        assertThatThrownBy(() -> subway.registerLine(new Line(name, "검정색")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("중복되는 이름의 노선이 이미 존재합니다.");
    }

    @Test
    void 존재하지_않는_노선을_삭제하면_예외가_발생한다() {
        // given
        final Subway subway = new Subway();

        // expect
        assertThatThrownBy(() -> subway.deleteStation("8호선", new Station("잠실역")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 노선입니다.");
    }

    @Test
    void 노선에_존재하지_않는_역을_삭제하면_예외가_발생한다() {
        // given
        final Line line = new Line("8호선", "분홍색");

        final Subway subway = new Subway(List.of(line));

        // expect
        assertThatThrownBy(() -> subway.deleteStation("8호선", new Station("강남역")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 역을 삭제할 수 없습니다.");
    }
}
