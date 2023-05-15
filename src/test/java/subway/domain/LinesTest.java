package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LinesTest {

    @Test
    void 모든_노선을_조회할_수_있다() {
        // given
        final Section firstSection = new Section("잠실역", "석촌역", 10);
        final Section secondSection = new Section("석촌역", "송파역", 10);
        final Section thirdSection = new Section("모란역", "야탑역", 10);
        final Section fourthSection = new Section("야탑역", "이매역", 10);

        final Line firstLine = new Line("8호선", "분홍색", List.of(firstSection, secondSection));
        final Line secondLine = new Line("분당선", "노란색", List.of(thirdSection, fourthSection));

        final Lines lines = new Lines(List.of(firstLine, secondLine));

        // when
        final List<Line> result = lines.getLines();

        // then
        assertThat(result).containsExactly(firstLine, secondLine);
    }


}