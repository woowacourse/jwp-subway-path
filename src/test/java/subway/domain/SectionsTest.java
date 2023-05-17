package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.service.domain.Distance;
import subway.service.domain.Section;
import subway.service.domain.Sections;
import subway.service.domain.Station;

import java.util.List;

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

        assertThat(sections.findContainsThisStation(nextStation))
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

        assertThat(sections.findContainsThisStation(standardStation))
                .hasSize(2)
                .allMatch(section -> section.getPreviousStation().equals(previousStation)
                        || section.getNextStation().equals(nextStation));
    }



}
