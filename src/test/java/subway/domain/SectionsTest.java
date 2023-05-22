package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SectionsTest {

    @Test
    void 첫_등록은_구간이_비어있어도_등록_가능하다() {
        // given
        final Sections sections = new Sections(List.of());

        // when
        sections.register(new Station("잠실역"), new Station("석촌역"), 10);

        // then
        assertThat(sections.get()).contains(new Section("잠실역", "석촌역", 10));
    }

    @Test
    void 등록시_두_역이_모두_존재하지_않으면_예외가_발생한다() {
        // given
        final Section section = new Section("잠실역", "석촌역", 5);
        final Sections sections = new Sections(List.of(section));

        // expect
        assertThatThrownBy(() -> sections.register(new Station("송파역"), new Station("몽촌토성역"), 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("기준역이 존재하지 않아 추가할 수 없습니다.");
    }

    @Test
    void 등록시_두_역이_모두_존재하면_예외가_발생한다() {
        // given
        final Section section = new Section("잠실역", "석촌역", 3);
        final Sections sections = new Sections(List.of(section));

        // expect
        assertThatThrownBy(() -> sections.register(new Station("잠실역"), new Station("석촌역"), 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("두 역 모두 노선에 존재하는 역입니다");
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 11})
    void 등록시_기준역이_구간의_출발점에_존재할_경우_등록하려는_구간의_거리는_기존_거리보다_짧지_않으면_예외가_발생한다(final int distance) {
        // given
        final Section section = new Section("잠실역", "석촌역", 10);

        final Sections sections = new Sections(List.of(section));

        // expect
        assertThatThrownBy(() -> sections.register(new Station("잠실역"), new Station("송파역"), distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록하려는 구간의 거리는 기존 구간의 거리보다 짧아야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 11})
    void 상행_등록시_기준역이_구간의_끝점에_존재할_경우_등록할_구간의_거리가_기존_구간의_거리보다_짧지_않으면_예외가_발생한다(final int distance) {
        // given
        final Section section = new Section("잠실역", "석촌역", 10);

        final Sections sections = new Sections(List.of(section));

        // expect
        assertThatThrownBy(() -> sections.register(new Station("송파역"), new Station("석촌역"), distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록하려는 구간의 거리는 기존 구간의 거리보다 짧아야 합니다.");
    }

    @Test
    void 이미_존재하는_구간_사이에_역을_등록하는_경우_거리_정보가_갱신된다() {
        // given
        final Section firstSection = new Section("잠실역", "석촌역", 10);
        final Section secondSection = new Section("석촌역", "송파역", 10);

        final Sections sections = new Sections(List.of(firstSection, secondSection));

        // when
        sections.register(new Station("잠실역"), new Station("가락시장역"), 5);

        // then
        assertThat(sections.get()).containsAll(List.of(
                new Section("잠실역", "가락시장역", 5),
                new Section("가락시장역", "석촌역", 5),
                new Section("석촌역", "송파역", 10)
        ));
    }

    @Test
    void 상행_종점을_등록한다() {
        // given
        final Section section = new Section("잠실역", "석촌역", 10);

        final Sections sections = new Sections(List.of(section));

        // when
        sections.register(new Station("몽촌토성역"), new Station("잠실역"), 5);

        // then
        assertThat(sections.get()).containsAll(List.of(
                new Section("몽촌토성역", "잠실역", 5),
                new Section("잠실역", "석촌역", 10)
        ));
    }

    @Test
    void 하행_종점을_등록한다() {
        // given
        final Section section = new Section("잠실역", "석촌역", 10);

        final Sections sections = new Sections(List.of(section));

        // when
        sections.register(new Station("석촌역"), new Station("송파역"), 5);

        // then
        assertThat(sections.get()).containsAll(List.of(
                new Section("잠실역", "석촌역", 10),
                new Section("석촌역", "송파역", 5)
        ));
    }

    @Test
    void 상행_종점을_제거한다() {
        // given
        final Section firstSection = new Section("잠실역", "석촌역", 10);
        final Section secondSection = new Section("석촌역", "송파역", 10);

        final Sections sections = new Sections(List.of(firstSection, secondSection));

        // when
        sections.delete(new Station("잠실역"));

        // then
        assertThat(sections.get()).contains(new Section("석촌역", "송파역", 10));
    }

    @Test
    void 하행_종점을_제거한다() {
        // given
        final Section firstSection = new Section("잠실역", "석촌역", 10);
        final Section secondSection = new Section("석촌역", "송파역", 10);

        final Sections sections = new Sections(List.of(firstSection, secondSection));

        // when
        sections.delete(new Station("송파역"));

        // then
        assertThat(sections.get()).contains(new Section("잠실역", "석촌역", 10));
    }

    @Test
    void 중간에_존재하는_역_제거시_양옆_역이_새로운_구간이_된다() {
        // given
        final Section firstSection = new Section("잠실역", "석촌역", 10);
        final Section secondSection = new Section("석촌역", "송파역", 10);

        final Sections sections = new Sections(List.of(firstSection, secondSection));

        // when
        sections.delete(new Station("석촌역"));

        // then
        assertThat(sections.get()).contains(new Section("잠실역", "송파역", 20));
    }

    @Test
    void 존재하지_않는_역을_삭제하면_예외가_발생한다() {
        // given
        final Section section = new Section("잠실역", "석촌역", 10);

        final Sections sections = new Sections(List.of(section));

        // expect
        assertThatThrownBy(() -> sections.delete(new Station("강남역")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 역을 삭제할 수 없습니다.");
    }

    @Test
    void 두_역이_등록된_경우_하나의_역을_제거시_모든_역을_제거한다() {
        // given
        final Section section = new Section("잠실역", "석촌역", 10);
        final Sections sections = new Sections(List.of(section));

        // when
        sections.delete(new Station("잠실역"));

        // then
        assertThat(sections.get()).isEmpty();
    }

    @Test
    void 역을_순서대로_조회한다() {
        // given
        final Section firstSection = new Section("잠실역", "석촌역", 10);
        final Section secondSection = new Section("석촌역", "송파역", 10);
        final Section thirdSection = new Section("송파역", "가락시장역", 10);
        final Section fourthSection = new Section("가락시장역", "문정역", 10);

        final Sections sections = new Sections(List.of(firstSection, secondSection, thirdSection, fourthSection));

        // when
        final List<Station> orderedStations = sections.getOrderedStations();

        // then
        assertThat(orderedStations).containsAll(
                List.of(
                        new Station("잠실역"),
                        new Station("석촌역"),
                        new Station("송파역"),
                        new Station("가락시장역"),
                        new Station("문정역")
                ));
    }
}