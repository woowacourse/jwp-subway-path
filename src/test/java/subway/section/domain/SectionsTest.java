package subway.section.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.station.domain.Station;
import subway.vo.Name;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {

    Sections sections;

    @BeforeEach
    void setUp() {
        Section section1 = Section.of(1L, 1L, 2L, 5);
        Section section2 = Section.of(2L, 2L, 3L, 5);
        this.sections = new Sections(List.of(section1, section2));
    }

    @DisplayName("가지고 있는 Stations를 오름차순(상행역 -> 하행역)으로 반환한다.")
    @Test
    void getStationsAsc() {
        // given
        final Station finalUpStation = Station.of(1L, Name.from("잠실역"));

        // when
        List<Station> stations = sections.getStationsAsc(finalUpStation);

        // then
        Assertions.assertAll(
                () -> assertThat(stations.get(0).getId()).isEqualTo(1L),
                () -> assertThat(stations.get(1).getId()).isEqualTo(2L),
                () -> assertThat(stations.size()).isEqualTo(2)
        );
    }
}
