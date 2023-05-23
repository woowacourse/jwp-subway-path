package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.NoSuchElementException;
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
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(
                        new Section("강남역", "역삼역", 10)
                ));
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

    @Test
    void 입력받은_노선의_역을_삭제한다() {
        // given
        Subway subway = new Subway();
        Line line = new Line("2호선", List.of(
                new Section("교대역", "강남역", 10),
                new Section("강남역", "역삼역", 5))
        );
        subway.addLine(line);

        // when
        subway.removeStation("2호선", "역삼역");
        Line findLine = subway.findLineByName("2호선");

        // then
        assertThat(findLine.getSections()).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(
                        new Section("교대역", "강남역", 10)
                ));
    }

    @Test
    void 노선에_역이_2개만_존재할_때_하나의_역을_삭제하면_역_전체가_삭제된다() {
        // given
        Subway subway = new Subway();
        Line line = new Line("2호선", List.of(new Section("교대역", "강남역", 10)));
        subway.addLine(line);

        // when
        subway.removeStation("2호선", "강남역");

        // then
        assertThat(subway.getLines()).flatExtracting(Line::getStations)
                .isEmpty();
    }

    @Test
    void 존재하지_않는_노선의_역을_삭제하면_예외가_발생한다() {
        // given
        Subway subway = new Subway();
        Line line = new Line("2호선", List.of(new Section("교대역", "강남역", 10)));
        subway.addLine(line);

        // when, then
        assertThatThrownBy(() -> subway.removeStation("1호선", "강남역"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("존재하지 않는 노선입니다.");
    }

    @Test
    void 노선에_존재하지_않는_역을_삭제하면_예외가_발생한다() {
        // given
        Subway subway = new Subway();
        Line line = new Line("2호선", List.of(new Section("교대역", "강남역", 10)));
        subway.addLine(line);

        // when, then
        assertThatThrownBy(() -> subway.removeStation("2호선", "역삼역"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("현재 삭제하려는 구간에는 노선에 존재하지 않는 역이 포함돼 있습니다.");
    }

    @Test
    void 이름으로_노선을_찾는다() {
        // given
        Subway subway = new Subway();
        Line line = new Line("1호선", List.of(new Section("서울역", "명동역", 10)));
        Line otherLine = new Line("2호선", List.of(new Section("교대역", "강남역", 10)));
        subway.addLine(line);
        subway.addLine(otherLine);

        // when
        Line findLine = subway.findLineByName("1호선");

        // then
        assertThat(findLine.getSections())
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(
                        new Section("서울역", "명동역", 10)
                ));
    }

    @Test
    void 존재하지_않는_이름으로_노선을_찾으면_예외가_발생한다() {
        // given
        Subway subway = new Subway();
        Line line = new Line("1호선", List.of(new Section("서울역", "명동역", 10)));
        subway.addLine(line);

        // when, then
        assertThatThrownBy(() -> subway.findLineByName("2호선"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("존재하지 않는 노선입니다.");
    }

    @Test
    void 환승역을_삭제하면_한_노선에서만_삭제되고_다른_노선에는_존재한다() {
        // given
        Subway subway = new Subway();
        Line firstLine = new Line("1호선", List.of(
                new Section("서울역", "강남역", 5),
                new Section("강남역", "명동역", 10)
        ));
        Line secondLine = new Line("2호선", List.of(
                new Section("교대역", "강남역", 7),
                new Section("강남역", "역삼역", 9)
        ));
        subway.addLine(firstLine);
        subway.addLine(secondLine);

        // when
        subway.removeStation("1호선", "강남역");
        Line findFirstLine = subway.findLineByName("1호선");
        Line findSecondLine = subway.findLineByName("2호선");

        // then
        assertThat(findFirstLine.getSections()).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(
                        new Section("서울역", "명동역", 15)
                ));
        assertThat(findSecondLine.getSections()).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(
                        new Section("교대역", "강남역", 7),
                        new Section("강남역", "역삼역", 9)
                ));
    }

}
