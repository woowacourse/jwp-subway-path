package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.domain.Direction.LEFT;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.exception.InvalidLineNameException;
import subway.exception.InvalidSectionException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SubwayTest {

    @Test
    void 역_추가시_전체_라인에서_등록할_구간이_존재하면_예외를_던진다() {
        // given
        final Line line1 = new Line("1호선", "RED", List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        ));
        final Line line2 = new Line("2호선", "RED", List.of(
                new Section("Z", "B", 5),
                new Section("B", "Y", 5)
        ));
        final Subway subway = new Subway(List.of(line1, line2));

        // expect
        assertThatThrownBy(
                () -> subway.add(
                        "1호선",
                        new Station("B"),
                        new Station("Y"),
                        new Distance(5),
                        LEFT
                ))
                .isInstanceOf(InvalidSectionException.class)
                .hasMessage("지하철 전체 노선에 이미 존재하는 구간입니다.");
    }

    @Test
    void 역_추가시_입력한_노선_이름이_존재하지_않으면_예외를_던진다() {
        // given
        final Subway subway = new Subway(Collections.emptyList());

        // expect
        assertThatThrownBy(
                () -> subway.add(
                        "1호선",
                        new Station("B"),
                        new Station("Y"),
                        new Distance(5),
                        LEFT
                ))
                .isInstanceOf(InvalidLineNameException.class)
                .hasMessage("존재하지 않는 노선 이름입니다.");
    }

    @Test
    void 노선에_구간이_정상적으로_등록된다() {
        // given
        final Subway subway = new Subway(List.of(
                new Line("1호선", "RED", List.of(
                        new Section("A", "B", 5),
                        new Section("B", "C", 5)
                )),
                new Line("2호선", "RED", List.of(
                        new Section("Z", "B", 5),
                        new Section("B", "Y", 5)
                ))
        ));

        // when
        subway.add("1호선", new Station("B"), new Station("D"), new Distance(3), LEFT);

        // then
        assertThat(subway.getLines()).flatExtracting(Line::getSections).containsAll(List.of(
                new Section("A", "D", 2),
                new Section("D", "B", 3),
                new Section("B", "C", 5),
                new Section("Z", "B", 5),
                new Section("B", "Y", 5)
        ));
    }

    @Test
    void 역_제거시_입력한_노선_이름이_존재하지_않으면_예외를_던진다() {
        // given
        final Subway subway = new Subway(Collections.emptyList());

        // expect
        assertThatThrownBy(() -> subway.remove("1호선", new Station("B")))
                .isInstanceOf(InvalidLineNameException.class)
                .hasMessage("존재하지 않는 노선 이름입니다.");
    }

    @Test
    void 노선에_구간이_정상적으로_제거된다() {
        // given
        final Subway subway = new Subway(List.of(
                new Line("1호선", "RED", List.of(
                        new Section("A", "B", 5),
                        new Section("B", "C", 5)
                ))
        ));

        // when
        subway.remove("1호선", new Station("B"));

        // then
        assertThat(subway.getLines()).flatExtracting(Line::getSections).containsAll(List.of(
                new Section("A", "C", 10)
        ));
    }
}
