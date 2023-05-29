package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JgraphtPathTest {

    private JgraphtPath jgraphtPath = new JgraphtPath();

    @Test
    public void 시작역과_도착역의_최단경로를_반환한다() {
        // given
        Sections sections = new Sections(List.of(
                new Section(new Station("교대"), new Station("강남"), 3),
                new Section(new Station("강남"), new Station("선릉"), 4),
                new Section(new Station("서초"), new Station("교대"), 5),
                new Section(new Station("선릉"), new Station("삼성"), 6),
                new Section(new Station("선정릉"), new Station("선릉"), 11),
                new Section(new Station("선릉"), new Station("한티"), 8),
                new Section(new Station("한티"), new Station("도곡"), 9),
                new Section(new Station("교대"), new Station("선정릉"), 3)
        ));

        // when, then
        assertSoftly(softly -> {
            List<String> shortestPath = jgraphtPath.getShortestPath(sections, "교대", "도곡");
            softly.assertThat(shortestPath).isEqualTo(List.of("교대", "강남", "선릉", "한티", "도곡"));

            shortestPath = jgraphtPath.getShortestPath(sections, "교대", "강남");
            softly.assertThat(shortestPath).isEqualTo(List.of("교대", "강남"));

            shortestPath = jgraphtPath.getShortestPath(sections, "교대", "선릉");
            softly.assertThat(shortestPath).isEqualTo(List.of("교대", "강남", "선릉"));

            shortestPath = jgraphtPath.getShortestPath(sections, "서초", "선릉");
            softly.assertThat(shortestPath).isEqualTo(List.of("서초", "교대", "강남", "선릉"));

            shortestPath = jgraphtPath.getShortestPath(sections, "선정릉", "선릉");
            softly.assertThat(shortestPath).isEqualTo(List.of("선정릉", "교대", "강남", "선릉"));

            shortestPath = jgraphtPath.getShortestPath(sections, "삼성", "선정릉");
            softly.assertThat(shortestPath).isEqualTo(List.of("삼성", "선릉", "강남", "교대", "선정릉"));

            shortestPath = jgraphtPath.getShortestPath(sections, "한티", "선정릉");
            softly.assertThat(shortestPath).isEqualTo(List.of("한티", "선릉", "강남", "교대", "선정릉"));
        });
    }

    @ParameterizedTest
    @CsvSource({"교대, 강남, 3", "교대, 선릉, 7", "교대, 서초, 5", "교대, 선릉, 7", "서초, 선릉, 12", "선정릉, 선릉, 10",
            "삼성, 선정릉, 16", "한티, 선정릉, 18", "교대, 도곡, 24"})
    void 시작역과_도착역_최단경로의_거리를_반환한다(final String from, final String to, final Integer length) {
        // given
        Sections sections = new Sections(List.of(
                new Section(new Station("교대"), new Station("강남"), 3),
                new Section(new Station("강남"), new Station("선릉"), 4),
                new Section(new Station("서초"), new Station("교대"), 5),
                new Section(new Station("선릉"), new Station("삼성"), 6),
                new Section(new Station("선정릉"), new Station("선릉"), 11),
                new Section(new Station("선릉"), new Station("한티"), 8),
                new Section(new Station("한티"), new Station("도곡"), 9),
                new Section(new Station("교대"), new Station("선정릉"), 3)
        ));

        // when, then
        assertThat(jgraphtPath.getShortestDistance(sections, from, to)).isEqualTo(new Distance(length));
    }

}
