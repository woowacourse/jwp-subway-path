package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class ShortestPathFinderTest {

    @Test
    void 최단_거리를_조회한다() {
        // given
        final Subway subway = new Subway(List.of(getSections1(), getSections2(), getSections3()));
        final ShortestPathFinder shortestPathFinder = new ShortestPathFinder(subway);

        // when
        final ShortestPath shortestPath = shortestPathFinder.find("잠실역", "신촌역");

        // then
        final List<Station> stations = List.of(new Station("잠실역"), new Station("아현역"), new Station("신촌역"));
        final ShortestPath expected = new ShortestPath(stations, 3L);
        assertThat(shortestPath).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
    }

    Sections getSections1() {
        final Station 잠실역 = new Station("잠실역");
        final Station 아현역 = new Station("아현역");

        final Section section = new Section(잠실역, 아현역, 1L);

        return new Sections(List.of(section));
    }

    Sections getSections2() {
        final Station 잠실역 = new Station("잠실역");
        final Station 신촌역 = new Station("신촌역");

        final Section section = new Section(잠실역, 신촌역, 10L);

        return new Sections(List.of(section));
    }

    Sections getSections3() {
        final Station 아현역 = new Station("아현역");
        final Station 신촌역 = new Station("신촌역");

        final Section section = new Section(아현역, 신촌역, 2L);

        return new Sections(List.of(section));
    }
}