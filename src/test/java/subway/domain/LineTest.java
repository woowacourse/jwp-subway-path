package subway.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.common.fixture.DomainFixture.디노;
import static subway.common.fixture.DomainFixture.디노_조앤;
import static subway.common.fixture.DomainFixture.로운;
import static subway.common.fixture.DomainFixture.조앤;
import static subway.common.fixture.DomainFixture.후추;
import static subway.common.fixture.DomainFixture.후추_디노;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineTest {

    //before : 후추 - 7 - 디노 - 4 - 조앤
    //after : 후추 - 5 - 로운 - 2 - 디노 - 4 - 조앤
    @Test
    void 호선에_역을_추가한다() {
        //given
        final Line line = new Line(1L, "일호선", "남색", List.of(후추_디노, 디노_조앤));

        //when
        final Line insertedLine = line.insert(로운, 디노, 2);

        //then
        assertSoftly(softly -> {
            final List<Section> sections = insertedLine.getSections();
            softly.assertThat(sections).contains(new Section(후추, 로운, 5), new Section(로운, 디노, 2), 디노_조앤);
        });
    }

    //before : 후추 - 7 - 디노 - 4 - 조앤
    //after : 후추 - 11 - 조앤
    @Test
    void 호선에_역을_제거한다() {
        //given
        final Line line = new Line(1L, "일호선", "남색", List.of(후추_디노, 디노_조앤));

        //when
        final Line deletedLine = line.delete(디노);

        //then
        assertSoftly(softly -> {
            final List<Section> sections = deletedLine.getSections();
            softly.assertThat(sections).contains(new Section(후추, 조앤, 11));
            softly.assertThat(sections).doesNotContain(후추_디노, 디노_조앤);
        });
    }

    @ParameterizedTest
    @CsvSource({"일호선, true", "이호선, false"})
    void 이름이_같은지_확인한다(final String name, final boolean expected) {
        //given
        final Line line = new Line(1L, "일호선", "남색", List.of(후추_디노, 디노_조앤));

        //when
        final boolean actual = line.hasSameName(new Line(1L, name, "남색", List.of(후추_디노, 디노_조앤)));

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 정렬된_역들을_반환한다() {
        //given
        final Line line = new Line(1L, "일호선", "남색", List.of(후추_디노, 디노_조앤));

        //when
        final List<Station> stations = line.getOrderedStations();

        //then
        assertThat(stations).containsExactly(후추, 디노, 조앤);
    }
}
