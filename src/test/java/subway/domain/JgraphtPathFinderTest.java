package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.domain.fixture.SectionFixtures.포함된_구간들을_검증한다;
import static subway.domain.fixture.StationFixture.사번역;
import static subway.domain.fixture.StationFixture.삼번역;
import static subway.domain.fixture.StationFixture.오번역;
import static subway.domain.fixture.StationFixture.육번역;
import static subway.domain.fixture.StationFixture.이번역;
import static subway.domain.fixture.StationFixture.일번역;
import static subway.domain.fixture.StationFixture.지름길;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.domain.path.JgraphtPathFinder;
import subway.domain.path.Path;
import subway.exception.NotFoundPathException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("JgraphtPathFinder 은(는)")
class JgraphtPathFinderTest {

    private final JgraphtPathFinder jgraphtPathFinder = new JgraphtPathFinder();

    @Test
    void 최단_경로를_반환한다() {
        // given
        final List<Section> sections1 = List.of(
                new Section(일번역, 이번역, 10),
                new Section(이번역, 삼번역, 11),
                new Section(삼번역, 사번역, 12),
                new Section(사번역, 오번역, 13),
                new Section(오번역, 육번역, 14)
        );
        final List<Section> sections2 = List.of(
                new Section(일번역, 지름길, 5),
                new Section(지름길, 육번역, 10)
        );

        final Lines lines = new Lines(List.of(
                new Line("1호선", new Sections(sections1)),
                new Line("2호선", new Sections(sections2))
        ));

        // when
        final Path path = jgraphtPathFinder.findShortestPath(일번역, 육번역, lines);

        // then
        포함된_구간들을_검증한다(path.getSections(),
                "1번역-[5km]-지름길",
                "지름길-[10km]-6번역"
        );
        assertThat(path.getTotalDistance()).isEqualTo(15);
    }

    @Test
    void 이어지지않은_경로를_요청시_예외_역이_이어진_노선은_있으나_연결되지않은_경우() {
        // given
        final List<Section> sections1 = List.of(
                new Section(일번역, 이번역, 10),
                new Section(이번역, 삼번역, 11),
                new Section(삼번역, 사번역, 12)
        );
        final List<Section> sections2 = List.of(
                new Section(지름길, 오번역, 15)
        );

        final Lines lines = new Lines(List.of(
                new Line("1호선", new Sections(sections1)),
                new Line("2호선", new Sections(sections2))
        ));

        // when & then
        assertThatThrownBy(() -> jgraphtPathFinder.findShortestPath(일번역, 오번역, lines))
                .isInstanceOf(NotFoundPathException.class);
    }

    @Test
    void 이어지지않은_경로를_요청시_예외_역은_존재하나_노선상에_없는_경우() {
        // given
        final List<Section> sections1 = List.of(
                new Section(일번역, 이번역, 10),
                new Section(이번역, 삼번역, 11),
                new Section(삼번역, 사번역, 12)
        );

        final Lines lines = new Lines(List.of(
                new Line("1호선", new Sections(sections1))
        ));

        // when & then
        assertThatThrownBy(() -> jgraphtPathFinder.findShortestPath(일번역, 오번역, lines))
                .isInstanceOf(NotFoundPathException.class);
    }
}
