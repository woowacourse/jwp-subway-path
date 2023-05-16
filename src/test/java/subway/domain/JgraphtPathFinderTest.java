package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.domain.fixture.SectionFixtures.포함된_구간들을_검증한다;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.exception.NotFoundPathException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("JgraphtPathFinder 은(는)")
class JgraphtPathFinderTest {

    private final JgraphtPathFinder jgraphtPathFinder = new JgraphtPathFinder();

    private static final Station ONE = new Station("1번역");
    private static final Station TWO = new Station("2번역");
    private static final Station THREE = new Station("3번역");
    private static final Station FOUR = new Station("4번역");
    private static final Station FIVE = new Station("5번역");
    private static final Station SIX = new Station("6번역");
    private static final Station 지름길 = new Station("지름길");

    @Test
    void 최단_경로를_반환한다() {
        // given
        List<Section> sections1 = List.of(
                new Section(ONE, TWO, 10),
                new Section(TWO, THREE, 11),
                new Section(THREE, FOUR, 12),
                new Section(FOUR, FIVE, 13),
                new Section(FIVE, SIX, 14)
        );
        List<Section> sections2 = List.of(
                new Section(ONE, 지름길, 5),
                new Section(지름길, SIX, 10)
        );

        List<Line> lines = List.of(
                new Line("1호선", new Sections(sections1)),
                new Line("2호선", new Sections(sections2))
        );

        // when
        Path path = jgraphtPathFinder.find(ONE, SIX, lines);

        // then
        assertThat(path.getTotalDistance()).isEqualTo(15);
        포함된_구간들을_검증한다(path.getSections(),
                "1번역-[5km]-지름길",
                "지름길-[10km]-6번역"
        );
    }

    @Test
    void 이어지지않은_경로를_요청시_예외() {
        // given
        List<Section> sections1 = List.of(
                new Section(ONE, TWO, 10),
                new Section(TWO, THREE, 11),
                new Section(THREE, FOUR, 12)
        );

        List<Section> sections2 = List.of(
                new Section(지름길, FIVE, 15)
        );

        List<Line> lines = List.of(
                new Line("1호선", new Sections(sections1)),
                new Line("2호선", new Sections(sections2))
        );

        // when & then
        assertThatThrownBy(() -> jgraphtPathFinder.find(ONE, FIVE, lines))
                .isInstanceOf(NotFoundPathException.class);
    }
}
