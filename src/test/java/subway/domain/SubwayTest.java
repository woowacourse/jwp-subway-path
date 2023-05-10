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
    void 노선을_등록한다() {
        // given
        Subway subway = new Subway();
        Line line = new Line("2호선", List.of(new Section("강남역", "역삼역", 10)));

        // when
        subway.addLine(line);

        //then
        assertThat(subway.getLines()).flatExtracting(Line::getSections)
                .containsExactly(new Section("강남역", "역삼역", 10));
    }

    @Test
    void 중복된_이름의_노선을_등록하면_예외가_발생한다() {
        // given
        Subway subway = new Subway();
        Line line = new Line("2호선", List.of(new Section("강남역", "역삼역", 10)));
        subway.addLine(line);
        Line sameNameLine = new Line("2호선", List.of(new Section("서초역", "교대역", 15)));

        // when, then
        assertThatThrownBy(() -> subway.addLine(sameNameLine))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복되는 이름의 노선이 이미 존재합니다.");
    }
}
