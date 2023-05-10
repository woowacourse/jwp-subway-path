package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.station.Station;

class SectionsTest {

    @Test
    @DisplayName("비어있으면 true를 반환한다.")
    void isEmpty_true_test() {
        // given
        final Sections sections = new Sections(new ArrayList<>());

        // expect
        assertThat(sections.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("비어있지 않으면 false를 반환한다.")
    void isEmpty_false_test() {
        // given
        final Station 잠실역 = new Station("잠실역");
        final Station 선릉역 = new Station("선릉역");
        final Section section = new Section(잠실역, 선릉역, 10, SectionType.UPWARD);
        final Sections sections = new Sections(List.of(section));

        // expect
        assertThat(sections.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("추가 요청받은 역 모두 다 노선에 존재하지 않으면 예외가 발생한다.")
    void validateSections_not_exist_test() {
        // given
        final Station 잠실역 = new Station("잠실역");
        final Station 선릉역 = new Station("선릉역");
        final Section section = new Section(잠실역, 선릉역, 10, SectionType.UPWARD);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(section);
        final Sections sections = new Sections(savedSections);

        final Station 판교역 = new Station("판교역");
        final Station 몽촌토성역 = new Station("몽촌토성역");
        final Section requestSection = new Section(판교역, 몽촌토성역, 5, SectionType.NORMAL);

        // expected
        assertThatThrownBy(() -> sections.validateSections(requestSection))
            .isInstanceOf(IllegalArgumentException.class)
            .extracting("message")
            .isEqualTo("존재하지 않는 역을 추가할 수 없습니다.");
    }

    @ParameterizedTest(name = "추가 요청받은 역 모두 다 노선에 존재하면 예외가 발생한다.")
    @CsvSource(value = {"잠실역:선릉역", "선릉역:잠실역"}, delimiter = ':')
    void validateSections_exist_test(final String sourceStationName, final String targetStationName) {
        // given
        final Station 잠실역 = new Station(sourceStationName);
        final Station 선릉역 = new Station(targetStationName);
        final Section section = new Section(잠실역, 선릉역, 10, SectionType.UPWARD);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(section);
        final Sections sections = new Sections(savedSections);

        final Station sourceStation = new Station(sourceStationName);
        final Station targetStation = new Station(targetStationName);
        final Section requestSection = new Section(sourceStation, targetStation, 5, SectionType.NORMAL);

        // expected
        assertThatThrownBy(() -> sections.validateSections(requestSection))
            .isInstanceOf(IllegalArgumentException.class)
            .extracting("message")
            .isEqualTo("이미 추가된 구간입니다.");
    }

    @ParameterizedTest(name = "요청받은 역이 상행 종점에 존재하는지 확인한다 name = {0} : {1}")
    @CsvSource(value = {"잠실역:true", "산성역:false"}, delimiter = ':')
    void isTargetUpward_test(final String stationName, final boolean expected) {
        // given
        final Station 잠실역 = new Station("잠실역");
        final Station 선릉역 = new Station("선릉역");
        final Section section = new Section(잠실역, 선릉역, 10, SectionType.UPWARD);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(section);
        final Sections sections = new Sections(savedSections);

        final Station station = new Station(stationName);

        // expected
        assertThat(sections.isTargetUpward(station)).isSameAs(expected);
    }

    @ParameterizedTest(name = "요청받은 역이 하행 종점에 존재하는지 확인한다 name = {0} : {1}")
    @CsvSource(value = {"선릉역:true", "산성역:false"}, delimiter = ':')
    void isTargetDownward_test(final String stationName, final boolean expected) {
        // given
        final Station 잠실역 = new Station("잠실역");
        final Station 선릉역 = new Station("선릉역");
        final Section section = new Section(잠실역, 선릉역, 10, SectionType.UPWARD);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(section);
        final Sections sections = new Sections(savedSections);

        final Station station = new Station(stationName);

        // expected
        assertThat(sections.isSourceDownward(station)).isSameAs(expected);
    }

    @Test
    @DisplayName("요청받은 시작역이 노선 구간의 시작역에 없으면 빈 값을 반환한다.")
    void getExistsSectionOfSource_empty_test() {
        // given
        final Station 잠실역 = new Station("잠실역");
        final Station 선릉역 = new Station("선릉역");
        final Section section = new Section(잠실역, 선릉역, 10, SectionType.UPWARD);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(section);
        final Sections sections = new Sections(savedSections);

        final Station 몽촌토성역 = new Station("몽촌토성역");
        final Section requestSection = new Section(몽촌토성역, 잠실역, 10, SectionType.NORMAL);

        // expected
        assertThat(sections.getExistsSectionOfSource(requestSection)).isEmpty();
    }

    @ParameterizedTest(name = "요청받은 시작역이 노선 구간의 시작역에 있지만, 요청받은 거리가 노선 구간 사이의 거리보다 더 크거나 같다면 예외가 발생한다.")
    @ValueSource(ints = {10, 11})
    void getExistsSectionOfSource_distance_exception_test(final int distance) {
        // given
        final Station 잠실역 = new Station("잠실역");
        final Station 선릉역 = new Station("선릉역");
        final Section section = new Section(잠실역, 선릉역, 10, SectionType.UPWARD);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(section);
        final Sections sections = new Sections(savedSections);

        final Station 몽촌토성역 = new Station("몽촌토성역");
        final Section requestSection = new Section(잠실역, 몽촌토성역, distance, SectionType.NORMAL);

        // expected
        assertThatThrownBy(() -> sections.getExistsSectionOfSource(requestSection))
            .isInstanceOf(IllegalArgumentException.class)
            .extracting("message")
            .isEqualTo("거리가 너무 커서 역을 추가할 수 없습니다.");
    }

    @Test
    @DisplayName("요청받은 시작역이 노선 구간의 시작역에 있고, 요청받은 거리가 노선 구간 사이의 거리보다 작다면 해당 구간을 반환한다.")
    void getExistsSectionOfSource_test() {
        // given
        final Station 잠실역 = new Station("잠실역");
        final Station 선릉역 = new Station("선릉역");
        final Section section = new Section(잠실역, 선릉역, 10, SectionType.UPWARD);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(section);
        final Sections sections = new Sections(savedSections);

        final Station 몽촌토성역 = new Station("몽촌토성역");
        final Section requestSection = new Section(잠실역, 몽촌토성역, 8, SectionType.NORMAL);

        // expected
        assertThat(sections.getExistsSectionOfSource(requestSection))
            .isEqualTo(Optional.ofNullable(section));
    }

    @Test
    @DisplayName("요청받은 끝역이 노선 구간의 끝역에 없으면 빈 값을 반환한다.")
    void getExistsSectionOfTarget_empty_test() {
        // given
        final Station 잠실역 = new Station("잠실역");
        final Station 선릉역 = new Station("선릉역");
        final Section section = new Section(잠실역, 선릉역, 10, SectionType.UPWARD);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(section);
        final Sections sections = new Sections(savedSections);

        final Station 몽촌토성역 = new Station("몽촌토성역");
        final Section requestSection = new Section(몽촌토성역, 잠실역, 10, SectionType.NORMAL);

        // expected
        assertThat(sections.getExistsSectionOfTarget(requestSection)).isEmpty();
    }

    @ParameterizedTest(name = "요청받은 끝역이 노선 구간의 끝역에 있지만, 요청받은 거리가 노선 구간 사이의 거리보다 더 크거나 같다면 예외가 발생한다.")
    @ValueSource(ints = {10, 11})
    void getExistsSectionOfTarget_distance_exception_test(final int distance) {
        // given
        final Station 잠실역 = new Station("잠실역");
        final Station 선릉역 = new Station("선릉역");
        final Section section = new Section(잠실역, 선릉역, 10, SectionType.UPWARD);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(section);
        final Sections sections = new Sections(savedSections);

        final Station 몽촌토성역 = new Station("몽촌토성역");
        final Section requestSection = new Section(몽촌토성역, 선릉역, distance, SectionType.NORMAL);

        // expected
        assertThatThrownBy(() -> sections.getExistsSectionOfTarget(requestSection))
            .isInstanceOf(IllegalArgumentException.class)
            .extracting("message")
            .isEqualTo("거리가 너무 커서 역을 추가할 수 없습니다.");
    }

    @Test
    @DisplayName("요청받은 끝이 노선 구간의 끝역에 있고, 요청받은 거리가 노선 구간 사이의 거리보다 작다면 해당 구간을 반환한다.")
    void getExistsSectionOfTarget_test() {
        // given
        final Station 잠실역 = new Station("잠실역");
        final Station 선릉역 = new Station("선릉역");
        final Section section = new Section(잠실역, 선릉역, 10, SectionType.UPWARD);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(section);
        final Sections sections = new Sections(savedSections);

        final Station 몽촌토성역 = new Station("몽촌토성역");
        final Section requestSection = new Section(몽촌토성역, 선릉역, 8, SectionType.NORMAL);

        // expected
        assertThat(sections.getExistsSectionOfTarget(requestSection))
            .isEqualTo(Optional.ofNullable(section));
    }
}
