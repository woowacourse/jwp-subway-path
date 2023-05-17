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
        int IGNORED = 10;
        Station previousStation = new Station("hello");
        Station nextStation = new Station("hell");
        Station existsStation = new Station("hello");
        Station notExistsStation = new Station("hel");
        Section section = new Section(previousStation, nextStation, Distance.from(IGNORED));

        assertThat(section.isContainsStation(existsStation)).isTrue();
        assertThat(section.isContainsStation(notExistsStation)).isFalse();
    }

    @Test
    @DisplayName("Section 의 Previous Station 이 해당 Station 인지 확인한다.")
    void isPreviousStationThisStation() {
        int IGNORED = 10;
        Station previousStation = new Station("hello");
        Station nextStation = new Station("hell");
        Station existsStation = new Station("hello");
        Station notExistsStation = new Station("hell");
        Section section = new Section(previousStation, nextStation, Distance.from(IGNORED));

        assertThat(section.isPreviousStationThisStation(existsStation)).isTrue();
        assertThat(section.isPreviousStationThisStation(notExistsStation)).isFalse();
    }

    @Test
    @DisplayName("Section 의 Next Station 이 해당 Station 인지 확인한다.")
    void isNextStationThisStation() {
        int IGNORED = 10;
        Station previousStation = new Station("hello");
        Station nextStation = new Station("hell");
        Station existsStation = new Station("hell");
        Station notExistsStation = new Station("hello");
        Section section = new Section(previousStation, nextStation, Distance.from(IGNORED));

        assertThat(section.isNextStationThisStation(existsStation)).isTrue();
        assertThat(section.isNextStationThisStation(notExistsStation)).isFalse();
    }

}
