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
        int ignored = 10;
        Station previousStation = new Station("hello");
        Station nextStation = new Station("hell");
        Station existsStation = new Station("hello");
        Station notExistsStation = new Station("hel");
        Section section = new Section(previousStation, nextStation, Distance.from(ignored));

        assertThat(section.isContainsStation(existsStation)).isTrue();
        assertThat(section.isContainsStation(notExistsStation)).isFalse();
    }

}
