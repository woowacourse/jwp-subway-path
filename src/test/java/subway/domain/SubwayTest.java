package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.fixture.SectionFixture.SECTION_1;
import static subway.fixture.SectionFixture.SECTION_2;
import static subway.fixture.StationFixture.*;

public class SubwayTest {
    @DisplayName("SubwayMap 을 생성한다")
    @Test
    void create() {
        //given
        Sections sections = new Sections(new ArrayList<>(List.of(SECTION_1, SECTION_2)));

        //when
        Subway subway = Subway.from(sections);

        //then
        assertThat(subway.getSubwayMap().get(STATION_1)).isEqualTo(List.of(SECTION_1));
        assertThat(subway.getSubwayMap().get(STATION_2)).isEqualTo(List.of(SECTION_1, SECTION_2));
        assertThat(subway.getSubwayMap().get(STATION_3)).isEqualTo(List.of(SECTION_2));
    }
}
