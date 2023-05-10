package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;

class SectionsTest {

    @Test
    @DisplayName("노선이 비어있으면 요청받은 역을 추가할 수 있다.")
    void add_empty_success_test() {
        // given
        final Sections sections = new Sections(new ArrayList<>());
        final Station 잠실역 = new Station("잠실역");
        final Station 선릉역 = new Station("선릉역");
        final Section section = new Section(잠실역, 선릉역, 10);

        // when
        sections.add(section);

        // then
        final Section findSection = sections.getSections().get(0);
        assertThat(findSection)
            .extracting("source", "target", "distance")
            .containsExactly(잠실역, 선릉역, 10);
    }

    @Test
    @DisplayName("추가 요청받은 역 모두 다 노선에 존재하지 않으면 예외가 발생한다.")
    void add_fail_test() {
        // given
        final Station 잠실역 = new Station("잠실역");
        final Station 선릉역 = new Station("선릉역");
        final Section section = new Section(잠실역, 선릉역, 10);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(section);
        final Sections sections = new Sections(savedSections);

        final Station 판교역 = new Station("판교역");
        final Station 몽촌토성역 = new Station("몽촌토성역");
        final Section requestSection = new Section(판교역, 몽촌토성역, 5);

        // expected
        assertThatThrownBy(() -> sections.add(requestSection))
            .isInstanceOf(IllegalArgumentException.class)
            .extracting("message")
            .isEqualTo("존재하지 않는 역을 추가할 수 없습니다");
    }

    @Test
    @DisplayName("추가 요청받은 시작 역이 존재하면 구간 정보에 추가한다.")
    void add_source_success_test() {
        // given
        final Station 잠실역 = new Station("잠실역");
        final Station 선릉역 = new Station("선릉역");
        final Section section = new Section(잠실역, 선릉역, 10);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(section);
        final Sections sections = new Sections(savedSections);

        final Station 몽촌토성역 = new Station("몽촌토성역");
        final Section requestSection = new Section(잠실역, 몽촌토성역, 5);

        // when
        sections.add(requestSection);

        // then
        final Section findSection = sections.getSections().get(1);
        assertThat(findSection)
            .extracting("source", "target", "distance")
            .containsExactly(잠실역, 몽촌토성역, 5);
    }

    @Test
    @DisplayName("추가 요청받은 끝 역이 존재하면 구간 정보에 추가한다.")
    void add_target_success_test() {
        // given
        final Station 잠실역 = new Station("잠실역");
        final Station 선릉역 = new Station("선릉역");
        final Section section = new Section(잠실역, 선릉역, 10);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(section);
        final Sections sections = new Sections(savedSections);

        final Station 몽촌토성역 = new Station("몽촌토성역");
        final Section requestSection = new Section(몽촌토성역, 잠실역, 5);

        // when
        sections.add(requestSection);

        // then
        final Section findSection = sections.getSections().get(1);
        assertThat(findSection)
            .extracting("source", "target", "distance")
            .containsExactly(몽촌토성역, 잠실역, 5);
    }
}
