package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.service.domain.Direction;
import subway.service.domain.Distance;
import subway.service.domain.LineProperty;
import subway.service.domain.Path;
import subway.service.domain.Section;
import subway.service.domain.Sections;
import subway.service.domain.Station;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {

    @Test
    @DisplayName("List<Section> 에서 해당 Station 을 포함하고 있는지 List 를 반환해준다. (1개 반환하는 경우)")
    void findContainsThisStation_returnSizeOne() {
        Distance IGNORED = Distance.from(10);
        Station previousStation = new Station("previous");
        Station nextStation = new Station("next");
        Sections sections = new Sections(List.of(
                new Section(previousStation, nextStation, IGNORED)));

        assertThat(sections.findContainsStation(nextStation))
                .hasSize(1)
                .allMatch(section -> section.getPreviousStation().equals(previousStation)
                        || section.getNextStation().equals(nextStation));
    }

    @Test
    @DisplayName("List<Section> 에서 해당 Station 을 포함하고 있는지 List 를 반환해준다. (2개 반환하는 경우)")
    void findContainsThisStation_returnSizeTwo() {
        Distance IGNORED = Distance.from(10);
        Station previousStation = new Station("previous");
        Station standardStation = new Station("standard");
        Station nextStation = new Station("next");
        Sections sections = new Sections(List.of(
                new Section(previousStation, standardStation, IGNORED),
                new Section(standardStation, nextStation, IGNORED)));

        assertThat(sections.findContainsStation(standardStation))
                .hasSize(2)
                .allMatch(section -> section.getPreviousStation().equals(previousStation)
                        || section.getNextStation().equals(nextStation));
    }

    @Test
    @DisplayName("현재 Section 중 해당 Station 을 가지고 있는 Station 이 있는지 boolean 형으로 반환한다.")
    void isContainsThisStation() {
        Distance IGNORED = Distance.from(10);
        Station previousStation = new Station("previous");
        Station standardStation = new Station("standard");
        Station notExistsStation = new Station("notExists");
        Sections sections = new Sections(List.of(
                new Section(previousStation, standardStation, IGNORED)));

        assertThat(sections.isContainsStation(previousStation)).isTrue();
        assertThat(sections.isContainsStation(standardStation)).isTrue();
        assertThat(sections.isContainsStation(notExistsStation)).isFalse();
    }
    
    @Test
    @DisplayName("현재 Section 중 해당 Station 을 PreviousStation 으로 가진 Section 을 반환한다.")
    void findPreviousStationThisStation() {
        Distance IGNORED = Distance.from(10);
        Station previousStation = new Station("previous");
        Station standardStation = new Station("standard");
        Station nextStation = new Station("next");
        Sections sections = new Sections(List.of(
                new Section(previousStation, standardStation, IGNORED),
                new Section(standardStation, nextStation, IGNORED)));

        Optional<Section> section = sections.findPreviousStationStation(standardStation);
        Optional<Section> emptySection = sections.findPreviousStationStation(nextStation);

        assertThat(emptySection).isEmpty();
        assertThat(section).isPresent();
        assertThat(section.get().getPreviousStation()).isEqualTo(standardStation);
        assertThat(section.get().getNextStation()).isEqualTo(nextStation);
    }

    @Test
    @DisplayName("현재 Section 중 해당 Station 을 PreviousStation 으로 가진 Section 을 반환한다.")
    void findNextStationThisStation() {
        Distance IGNORED = Distance.from(10);
        Station previousStation = new Station("previous");
        Station standardStation = new Station("standard");
        Station nextStation = new Station("next");
        Sections sections = new Sections(List.of(
                new Section(previousStation, standardStation, IGNORED),
                new Section(standardStation, nextStation, IGNORED)));

        Optional<Section> section = sections.findNextStationStation(standardStation);
        Optional<Section> emptySection = sections.findNextStationStation(previousStation);

        assertThat(emptySection).isEmpty();
        assertThat(section).isPresent();
        assertThat(section.get().getNextStation()).isEqualTo(standardStation);
        assertThat(section.get().getPreviousStation()).isEqualTo(previousStation);
    }

    @Test
    @DisplayName("Sections 가 Section 을 이용해 움직일 수 있는 Map 을 반환한다.")
    void createMap() {
        Distance IGNORED_DISTANCE = Distance.from(10);
        LineProperty IGNORED_LINE_PROPERTY = new LineProperty(1L, "1", "1");
        Station previousStation = new Station("previous");
        Station nextStation = new Station("next");
        Sections sections = new Sections(List.of(
                new Section(previousStation, nextStation, IGNORED_DISTANCE)));

        Map<Station, List<Path>> map = sections.createMap(IGNORED_LINE_PROPERTY).getMap();
        Path previousToNext = map.get(previousStation).get(0);
        Path nextToPrevious = map.get(nextStation).get(0);

        assertThat(map).hasSize(2);
        assertThat(previousToNext.getDirection()).isEqualTo(Direction.UP);
        assertThat(previousToNext.getNextStation()).isEqualTo(nextStation);
        assertThat(nextToPrevious.getDirection()).isEqualTo(Direction.DOWN);
        assertThat(nextToPrevious.getNextStation()).isEqualTo(previousStation);
    }

}
