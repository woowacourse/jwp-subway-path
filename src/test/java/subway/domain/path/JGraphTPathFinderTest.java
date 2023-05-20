package subway.domain.path;

import static org.assertj.core.api.Assertions.*;
import static subway.TestSource.*;

import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JGraphTPathFinderTest {

    private final PathFinder pathFinder = new JGraphTPathFinder();

    @Test
    void 전체_노선에_포함되지_않은_역을_통한_객체_생성_테스트() {
        // given
        // 장지 - 10 - 잠실 - 10 - 천호
        Sections sections = new Sections(line8Sections);

        // when & then
        assertThatThrownBy(() -> pathFinder.findPath(sections, cheonho, gangnam))
            .isInstanceOf(PathException.class)
            .hasMessage("노선과 연결되지 않은 역이 입력되었습니다.");
    }

    @Test
    void 노선에서_연결되지_않은_역을_통한_객체_생성_테스트() {
        // given
        // 천호 - 10 - 장지
        // 건대 - 10 - 강남
        Section cheonhoJangji = new Section(1L, cheonho, jangji, pink, 10);
        Section kundaeGangnam = new Section(2L, kundae, gangnam, green, 10);
        Sections sections = new Sections(List.of(cheonhoJangji, kundaeGangnam));

        // when & then
        assertThatThrownBy(() -> pathFinder.findPath(sections, cheonho, gangnam))
            .isInstanceOf(PathException.class)
            .hasMessage("두 역이 연결되지 않았습니다.");
    }

    @Test
    void 환승을_통한_최단거리_탐색_테스트1() {
        // given (천호 -> 잠실)
        //                      왕십리
        //                  /    |
        //              /        |
        //           11          10
        //        /              |
        //     /                 |
        // [잠실] - 10 - 몽촌 - 10 - [천호]
        Section wangsimniCheonho10 = new Section(1L, wangsimni, cheonho, purple, 10);
        Section wangsimniJamsil11 = new Section(2L, wangsimni, jamsil, green, 11);
        Section cheonhoMongchon10 = new Section(3L, cheonho, mongchon, pink, 10);
        Section mongchonJamsil10 = new Section(4L, mongchon, jamsil, pink, 10);
        Sections sections = new Sections(
            List.of(wangsimniCheonho10, wangsimniJamsil11, cheonhoMongchon10, mongchonJamsil10));

        // when
        PathInfo pathinfo = pathFinder.findPath(sections, cheonho, jamsil);

        // then
        List<Station> verticies = pathinfo.getPathVerticies();
        List<Section> edges = pathinfo.getPathEdges().getSections();
        assertThat(verticies).containsExactly(cheonho, mongchon, jamsil);
        assertThat(edges).containsExactly(cheonhoMongchon10, mongchonJamsil10);
    }

    @Test
    void 환승을_통한_최단거리_탐색_테스트2() {
        // given (잠실 -> 천호)
        //                      왕십리
        //                  /    |
        //              /        |
        //           10          10
        //        /              |
        //     /                 |
        // [잠실] - 10 - 몽촌 - 11 - [천호]
        Section wangsimniCheonho10 = new Section(1L, wangsimni, cheonho, purple, 10);
        Section wangsimniJamsil10 = new Section(2L, wangsimni, jamsil, green, 10);
        Section cheonhoMongchon11 = new Section(3L, cheonho, mongchon, pink, 11);
        Section mongchonJamsil10 = new Section(4L, mongchon, jamsil, pink, 10);
        Sections sections = new Sections(
            List.of(wangsimniCheonho10, wangsimniJamsil10, cheonhoMongchon11, mongchonJamsil10));

        // when
        PathInfo pathinfo = pathFinder.findPath(sections, jamsil, cheonho);

        // then
        List<Station> verticies = pathinfo.getPathVerticies();
        List<Section> edges = pathinfo.getPathEdges().getSections();
        assertThat(verticies).containsExactly(jamsil, wangsimni, cheonho);
        assertThat(edges).containsExactly(wangsimniJamsil10, wangsimniCheonho10);
    }
}
