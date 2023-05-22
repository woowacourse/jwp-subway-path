package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.service.domain.Distance;
import subway.service.domain.Section;
import subway.service.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;

class SectionTest {

    @Test
    @DisplayName("Section 에 해당 역이 포함이 되어 있는지 확인한다.")
    void isContainsStation() {
        Distance IGNORED = Distance.from(10);
        Station previousStation = new Station("hello");
        Station nextStation = new Station("hell");
        Station existsStation = new Station("hello");
        Station notExistsStation = new Station("hel");
        Section section = new Section(previousStation, nextStation, IGNORED);

        assertThat(section.isContainsStation(existsStation)).isTrue();
        assertThat(section.isContainsStation(notExistsStation)).isFalse();
    }

    @Test
    @DisplayName("Section 의 Previous Station 이 해당 Station 인지 확인한다.")
    void isPreviousStationThisStation() {
        Distance IGNORED = Distance.from(10);
        Station previousStation = new Station("hello");
        Station nextStation = new Station("hell");
        Station existsStation = new Station("hello");
        Station notExistsStation = new Station("hell");
        Section section = new Section(previousStation, nextStation, IGNORED);

        assertThat(section.isPreviousStationStation(existsStation)).isTrue();
        assertThat(section.isPreviousStationStation(notExistsStation)).isFalse();
    }

    @Test
    @DisplayName("Section 의 Next Station 이 해당 Station 인지 확인한다.")
    void isNextStationThisStation() {
        Distance IGNORED = Distance.from(10);
        Station previousStation = new Station("hello");
        Station nextStation = new Station("hell");
        Station existsStation = new Station("hell");
        Station notExistsStation = new Station("hello");
        Section section = new Section(previousStation, nextStation, IGNORED);

        assertThat(section.isNextStationStation(existsStation)).isTrue();
        assertThat(section.isNextStationStation(notExistsStation)).isFalse();
    }

}
