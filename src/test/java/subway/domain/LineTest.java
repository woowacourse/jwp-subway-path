package subway.domain;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineTest {

    @NullAndEmptySource
    @ParameterizedTest
    void 이름에는_공백이나_null이_들어올_수_없다(String value) {
        // when, then
        assertThatThrownBy(() -> new Line(value, Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아름에는 빈 문자가 들어올 수 없습니다.");
    }

    @Test
    void 역을_등록한다() {
        // given
        Line line = new Line("1호선", of(new Section("강남역", "역삼역", 10)));

        //when
        line.addSection(new Section("역삼역", "선릉역", 5));

        //then
        assertThat(line.getSections()).hasSize(2);
    }

    @Test
    void 등록하려는_역이_이미_모두_존재하면_예외가_발생한다() {
        // given
        Line line = new Line("1호선", of(new Section("강남역", "역삼역", 10), new Section("역삼역", "선릉역", 5)));

        // when, then
        assertThatThrownBy(() -> line.addSection(new Section("강남역", "선릉역", 15)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("두 역이 이미 모두 존재합니다.");
    }

    @Test
    void 구간을_등록할_때_기존의_구간_사이에_등록할_수_있다() {
        // given
        Line line = new Line("1호선", of(new Section("강남역", "역삼역", 10), new Section("역삼역", "삼성역", 5)));

        // when
        line.addSection(new Section("역삼역", "선릉역", 2));

        // then
        assertThat(line.getSections()).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(
                        new Section("강남역", "역삼역", 10),
                        new Section("역삼역", "선릉역", 2),
                        new Section("선릉역", "삼성역", 3)
                ));
    }

    @Test
    void 노선의_상행종점을_등록한다() {
        // given
        Line line = new Line("1호선", of(new Section("강남역", "역삼역", 10), new Section("역삼역", "삼성역", 5)));

        // when
        line.addSection(new Section("교대역", "강남역", 20));

        // then
        assertThat(line.getSections())
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(
                        new Section("강남역", "역삼역", 10),
                        new Section("역삼역", "삼성역", 5),
                        new Section("교대역", "강남역", 20)
                ));
    }

    @Test
    void 노선의_하행종점을_등록한다() {
        // given
        Line line = new Line("1호선", of(new Section("강남역", "역삼역", 10), new Section("역삼역", "삼성역", 5)));

        // when
        line.addSection(new Section("삼성역", "종합운동장역", 20));

        // then
        assertThat(line.getSections()).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(
                        new Section("강남역", "역삼역", 10),
                        new Section("역삼역", "삼성역", 5),
                        new Section("삼성역", "종합운동장역", 20)
                ));
    }

    @Test
    void 기존_구간의_사이에_거리를_초과하는_구간이_추가되면_예외가_발생한다() {
        // given
        Line line = new Line("1호선", of(new Section("강남역", "역삼역", 10)));

        // when, then
        assertThatThrownBy(() -> line.addSection(new Section("강남역", "선릉역", 20)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 양수만 가능합니다.");
    }

    @Test
    void 하나의_역은_여러_노선에_등록될_수_있다() {
        // given
        Line line = new Line("1호선", of(new Section("강남역", "역삼역", 10)));

        // when
        Line otherLine = new Line("2호선", of(new Section("강남역", "교대역", 15)));

        // then
        assertThat(line.getSections()).map(Section::getSource)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(
                        new Station("강남역")
                ));
        assertThat(otherLine.getSections()).map(Section::getSource)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(
                        new Station("강남역")
                ));
    }

    @Test
    void 삭제할_구간이_노선에_존재하지_않으면_예외가_발생한다() {
        // given
        Line line = new Line("1호선", of(new Section("강남역", "역삼역", 10), new Section("역삼역", "삼성역", 5)));
        Station nonExistStation = new Station("교대역");

        // when
        assertThatThrownBy(() -> line.removeStation(nonExistStation))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("현재 삭제하려는 구간에는 노선에 존재하지 않는 역이 포함돼 있습니다.");
    }


    @Test
    void 노선에_역을_삭제하면_두_역이_이어지고_거리가_더해진다() {
        // given
        Line line = new Line("1호선", of(new Section("강남역", "역삼역", 10), new Section("역삼역", "삼성역", 5)));

        // when
        line.removeStation(new Station("역삼역"));

        //then
        assertThat(line.getSections())
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(
                        new Section("강남역", "삼성역", 15)
                ));
    }

    @Test
    void 상행종점을_삭제한다() {
        // given
        Line line = new Line("1호선", of(
                new Section("강남역", "역삼역", 10),
                new Section("역삼역", "삼성역", 5))
        );

        // when
        line.removeStation(new Station("강남역"));

        //then
        assertThat(line.getSections())
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(
                        new Section("역삼역", "삼성역", 5)
                ));
    }

    @Test
    void 하행종점을_삭제한다() {
        // given
        Line line = new Line("1호선", of(
                new Section("강남역", "역삼역", 10),
                new Section("역삼역", "삼성역", 5))
        );

        // when
        line.removeStation(new Station("삼성역"));

        //then
        assertThat(line.getSections()).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(
                        new Section("강남역", "역삼역", 10)
                ));
    }

    @Test
    void 정렬된_역들을_조회한다() {
        // given
        Line line = new Line("1호선", of(
                new Section("역삼역", "삼성역", 5),
                new Section("강남역", "역삼역", 10),
                new Section("신림역", "강남역", 7)
        ));

        // when
        List<Station> stations = line.getStations();

        // then
        assertThat(stations).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(
                        new Station("신림역"),
                        new Station("강남역"),
                        new Station("역삼역"),
                        new Station("삼성역")
                ));
    }

    @Test
    void 연결되지_않은_Section_목록으로_Line을_생성하면_예외가_발생한다() {
        assertThatThrownBy(() ->
                new Line(
                        "1호선",
                        of(new Section("역삼역", "삼성역", 5), new Section("교대역", "강남역", 10))
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("연결되지 않은 역이 있습니다");
    }

    @Test
    void 연결되지_않는_역을_추가하면_예외가_발생한다() {
        // given
        Line line = new Line("1호선", of(
                new Section("역삼역", "삼성역", 5),
                new Section("강남역", "역삼역", 10),
                new Section("신림역", "강남역", 7)
        ));

        // when , then
        assertThatThrownBy(() -> line.addSection(new Section("서면역", "부암역", 10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("연결되지 않은 역이 있습니다");
    }
}
