package subway.domain.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.exception.InvalidSectionException;
import subway.exception.LineNotEmptyException;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SubwayTest {

    @Test
    void 역_추가시_전체_라인에서_등록할_구간이_존재하면_예외를_던진다() {
        // given
        final Line line1 = new Line("1호선", "RED", 0, List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        ));
        final Line line2 = new Line("2호선", "RED", 0, List.of(
                new Section("Z", "B", 5),
                new Section("B", "Y", 5)
        ));
        final Subway subway = new Subway(List.of(line1, line2));

        // expect
        assertThatThrownBy(() -> subway.add("1호선", "B", "Y", 5, "LEFT"))
                .isInstanceOf(InvalidSectionException.class)
                .hasMessage("지하철 전체 노선에 이미 존재하는 구간입니다.");
    }

    @Test
    void 역_추가시_입력한_노선_이름이_존재하지_않으면_예외를_던진다() {
        // given
        final Subway subway = new Subway(Collections.emptyList());

        // expect
        assertThatThrownBy(() -> subway.add("1호선", "B", "Y", 5, "LEFT"))
                .isInstanceOf(LineNotFoundException.class)
                .hasMessage("노선을 찾을 수 없습니다.");
    }

    @Test
    void 노선에_구간이_정상적으로_등록된다() {
        // given
        final Subway subway = new Subway(List.of(
                new Line("1호선", "RED", 0, List.of(
                        new Section("A", "B", 5),
                        new Section("B", "C", 5)
                )),
                new Line("2호선", "RED", 0, List.of(
                        new Section("Z", "B", 5),
                        new Section("B", "Y", 5)
                ))
        ));

        // when
        subway.add("1호선", "B", "D", 3, "LEFT");

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
        assertThatThrownBy(() -> subway.remove("1호선", "B"))
                .isInstanceOf(LineNotFoundException.class)
                .hasMessage("노선을 찾을 수 없습니다.");
    }

    @Test
    void 노선에_구간이_정상적으로_제거된다() {
        // given
        final Subway subway = new Subway(List.of(
                new Line("1호선", "RED", 0, List.of(
                        new Section("A", "B", 5),
                        new Section("B", "C", 5)
                ))
        ));

        // when
        subway.remove("1호선", "B");

        // then
        assertThat(subway.getLines()).flatExtracting(Line::getSections).containsAll(List.of(
                new Section("A", "C", 10)
        ));
    }

    @Test
    void 노선에_비어있을_때_초기_구간을_추가한다() {
        // given
        final Subway subway = new Subway(List.of(
                new Line("2호선", "RED", 0, Collections.emptyList())
        ));

        // when
        subway.initialAdd("2호선", "A", "B", 4);

        // then
        assertThat(subway.getLines()).flatExtracting(Line::getSections).containsAll(List.of(
                new Section("A", "B", 4)
        ));
    }

    @Test
    void 초기_구간을_추가할_때_노선이_비어있지_않은_경우_예외를_던진다() {
        // given
        final Subway subway = new Subway(List.of(
                new Line("2호선", "RED", 0, List.of(
                        new Section("B", "C", 3)
                ))
        ));

        // expect
        assertThatThrownBy(() -> subway.initialAdd("2호선", "A", "B", 4))
                .isInstanceOf(LineNotEmptyException.class)
                .hasMessage("노선이 비어있지 않습니다.");
    }

    @Test
    void 초기_구간을_추가할_때_이름이_같은_역을_추가하려는_경우_예외를_던진다() {
        // given
        final Subway subway = new Subway(List.of(
                new Line("2호선", "RED", 0, Collections.emptyList())
        ));

        // expect
        assertThatThrownBy(() -> subway.initialAdd("2호선", "A", "A", 4))
                .isInstanceOf(InvalidSectionException.class)
                .hasMessage("동일한 이름을 가진 역을 구간에 추가할 수 없습니다.");
    }

    @Test
    void 노선의_이름을_입력받아_해당_이름에_해당되는_노선을_반환한다() {
        // given
        final Subway subway = new Subway(List.of(
                new Line("1호선", "RED", 0, Collections.emptyList()),
                new Line("2호선", "BLUE", 0, Collections.emptyList())
        ));

        // when
        final Line line = subway.findLineByName("1호선");

        // expect
        assertThat(line.getName()).isEqualTo("1호선");
    }

    @Test
    void 노선의_이름을_입력받아_해당_이름에_해당되는_노선이_존재하지_않는다면_예외를_던진다() {
        // given
        final Subway subway = new Subway(List.of(
                new Line("2호선", "BLUE", 0, Collections.emptyList())
        ));

        // expect
        assertThatThrownBy(() -> subway.findLineByName("1호선"))
                .isInstanceOf(LineNotFoundException.class)
                .hasMessage("노선을 찾을 수 없습니다.");
    }

    @Test
    void 역_이름을_입력받아_해당_이름에_해당되는_역이_존재하지_않는다면_예외를_던진다() {
        // given
        final Subway subway = new Subway(List.of(
                new Line("2호선", "BLUE", 0, List.of(new Section("A", "B", 3)))
        ));

        // expect
        assertThatThrownBy(() -> subway.findStationByName("C"))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessage("역을 찾을 수 없습니다.");
    }

    @Test
    void 역_이름을_입력받아_해당_이름에_해당되는_역을_반환한다() {
        // given
        final Subway subway = new Subway(List.of(
                new Line("2호선", "BLUE", 0, List.of(new Section("A", "B", 3)))
        ));

        // when
        final Station result = subway.findStationByName("A");

        // then
        assertThat(result.getName()).isEqualTo("A");
    }

    @Test
    void 모든_역을_반환한다() {
        // given
        final Subway subway = new Subway(List.of(
                new Line("2호선", "BLUE", 0, List.of(new Section("A", "B", 3)))
        ));

        // when
        final List<Station> result = subway.getStations();

        // then
        assertThat(result).extracting(Station::getName).contains("A", "B");
    }
}
