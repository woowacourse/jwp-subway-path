package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.fixture.SectionFixture.*;
import static subway.fixture.StationFixture.*;

class SortedStationsTest {
    @DisplayName("하행 순서로 정렬된 역들의 리스트를 반환한다")
    @Test
    void sortedStations() {
        //given
        Sections sections = new Sections(new ArrayList<>(List.of(SECTION_2, SECTION_1, SECTION_3)));
        List<Station> expected = List.of(STATION_1, STATION_2, STATION_3, STATION_4);

        //when
        List<Station> stations = SortedStations.from(sections).getStations();

        //that
        assertThat(stations).isEqualTo(expected);
    }
}
