package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.controller.exception.StationNotFoundException;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class NavigationTest {

    @Test
    void 최단_경로를_조회한다() {
        // given
        final Section firstSection = new Section("잠실역", "석촌역", 2);
        final Section secondSection = new Section("석촌역", "송파역", 2);
        final Section thirdSection = new Section("송파역", "가락시장역", 10);
        final SubwayNavigation navigation = SubwayNavigation.from(List.of(
                new Sections(List.of(firstSection, secondSection, thirdSection)))
        );

        // when
        final List<Station> shortestPath = navigation.getShortestPath(new Station("잠실역"), new Station("송파역"));

        // then
        assertThat(shortestPath).isEqualTo(List.of(new Station("잠실역"), new Station("석촌역"), new Station("송파역")));
    }

    @Test
    void 출발역이_노선에_존재하지_않을_경우_예외가_발생한다() {
        // given
        final SubwayNavigation navigation = SubwayNavigation.from(Collections.emptyList());

        // expect
        assertThatThrownBy(() -> navigation.getShortestPath(new Station("잠실역"), new Station("석촌역")))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessageContaining("출발역이 존재하지 않습니다.");
    }

    @Test
    void 도착역이_노선에서_존재하지_않는_경우_예외가_발생한다() {
        // given
        final Section sections = new Section("잠실역", "석촌역", 2);
        final SubwayNavigation navigation = SubwayNavigation.from(List.of(
                new Sections(List.of(sections)))
        );

        // expect
        assertThatThrownBy(() -> navigation.getShortestPath(new Station("잠실역"), new Station("터틀역")))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessageContaining("도착역이 존재하지 않습니다.");
    }

    @Test
    void 최단경로의_거리를_구할_수_있다() {
        // given
        final Section firstSection = new Section("잠실역", "석촌역", 2);
        final Section secondSection = new Section("석촌역", "송파역", 2);
        final Section thirdSection = new Section("송파역", "가락시장역", 10);
        final SubwayNavigation navigation = SubwayNavigation.from(List.of(
                new Sections(List.of(firstSection, secondSection, thirdSection)))
        );

        // when
        final int distance = navigation.getDistance(new Station("잠실역"), new Station("송파역"));

        // then
        assertThat(distance).isEqualTo(4);
    }
}
