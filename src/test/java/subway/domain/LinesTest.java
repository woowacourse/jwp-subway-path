package subway.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.section.Section;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.common.fixture.DomainFixture.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LinesTest {

    @Test
    void 호선을_추가한다() {
        //given
        final Lines lines = new Lines(new ArrayList<>());

        //when
        final Lines insertedLines = lines.insert(new Line(1L, "일호선", "남색", new ArrayList<>()));

        //then
        assertSoftly(softly -> {
            final List<Line> allLines = insertedLines.getLines();
            softly.assertThat(allLines).hasSize(1);
            softly.assertThat(allLines.get(0)).isEqualTo(new Line(1L, "일호선", "남색", new ArrayList<>()));
        });
    }

    @Test
    void 이름이_중복되는_호선은_추가할_수_없다() {
        //given
        final Lines lines = new Lines(List.of(일호선_남색_후추_디노_조앤));

        //expect
        assertThatThrownBy(() -> lines.insert(new Line(2L, "일호선", "초록색", new ArrayList<>())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 호선입니다.");
    }

    @Test
    void 호선에_역을_추가한다() {
        //given
        final Line line = new Line(1L, "일호선", "남색", (List.of(후추_디노, 디노_조앤)));
        final Lines lines = new Lines(List.of(line));

        //when
        final Lines insertedLines = lines.insertStation(line, 로운, 디노, 2);

        //then
        assertSoftly(softly -> {
            final List<Line> allLines = insertedLines.getLines();
            final List<Section> sections = allLines.get(0).getSections();

            softly.assertThat(allLines).hasSize(1);
            softly.assertThat(sections).contains(new Section(후추, 로운, 5), new Section(로운, 디노, 2), 디노_조앤);
            softly.assertThat(sections).doesNotContain(후추_디노);
        });
        System.out.println("이거 뭐임 = " + 일호선_남색_후추_디노_조앤);
    }

    @Test
    void 존재하지_않는_호선에_역을_추가하면_예외를_던진다() {
        //given
        final Lines lines = new Lines(new ArrayList<>());

        //expect
        assertThatThrownBy(() -> lines.insertStation(일호선_남색_후추_디노_조앤, 로운, 디노, 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 호선입니다.");
        System.out.println("이거 뭐임 = " + 일호선_남색_후추_디노_조앤);
    }

    @Test
    void 호선의_역을_삭제한다() {
        //given
        final Line line = new Line(1L, "일호선", "남색", List.of(후추_디노, 디노_조앤));
        final Lines lines = new Lines(List.of(line));

        //when
        final Lines insertedLines = lines.deleteStation(line, 디노);

        //then
        assertSoftly(softly -> {
            final List<Section> sections = insertedLines.getLines().get(0).getSections();
            softly.assertThat(sections).contains(new Section(후추, 조앤, 11));
            softly.assertThat(sections).doesNotContain(후추_디노, 디노_조앤);
        });
    }

    @Test
    void 존재하지_않는_호선의_역을_삭제하면_예외를_던진다() {
        //given
        final Lines lines = new Lines(new ArrayList<>());

        //expect
        assertThatThrownBy(() -> lines.deleteStation(일호선_남색_후추_디노_조앤, 디노))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 호선입니다.");
    }
}
