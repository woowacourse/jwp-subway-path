package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.exception.BusinessException;

class LineTest {

    @Test
    @DisplayName("빈 호선의 맨 위에 역을 추가한다.")
    void testAddTopSectionWhenEmpty() {
        //given
        final Sections sections = new Sections(Collections.emptyList());
        final Line line = new Line("name", "color", sections);
        final Station station = new Station("station");

        //when
        //then
        assertThatThrownBy(() -> line.addTopStation(station))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("비어있지 않은 호선의 맨 위에 역을 추가한다.")
    void testAddTopSectionWhenNotEmpty() {
        //given
        final Station firstStation = new Station("firstStation");
        final Station secondStation = new Station("secondStation");
        final Station thirdStation = new Station("thirdStation");
        final Section section1 = new Section(firstStation, secondStation);
        final Section section2 = new Section(secondStation, thirdStation);
        final Sections sections = new Sections(new ArrayList<>(List.of(section1, section2)));
        final Line line = new Line("name", "color", sections);
        final Station station = new Station("station");

        //when
        line.addTopStation(station);

        //then
        assertThat(line.getStationsSize()).isEqualTo(4);
        assertThat(line.getSections().getTopStation()).isEqualTo(station);
    }

    @Test
    @DisplayName("빈 호선의 맨 아래에 역을 추가한다.")
    void testAddBottomSectionWhenEmpty() {
        //given
        final Sections sections = new Sections(Collections.emptyList());
        final Line line = new Line("name", "color", sections);
        final Station station = new Station("station");

        //when
        //then
        assertThatThrownBy(() -> line.addBottomStation(station))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("비어있지 않은 호선의 맨 아래에 역을 추가한다.")
    void testAddBottomSectionWhenNotEmpty() {
        //given
        final Station firstStation = new Station("firstStation");
        final Station secondStation = new Station("secondStation");
        final Station thirdStation = new Station("thirdStation");
        final Section section1 = new Section(firstStation, secondStation);
        final Section section2 = new Section(secondStation, thirdStation);
        final Sections sections = new Sections(new ArrayList<>(List.of(section1, section2)));
        final Line line = new Line("name", "color", sections);
        final Station station = new Station("station");

        //when
        line.addBottomStation(station);

        //then
        assertThat(line.getStationsSize()).isEqualTo(4);
        assertThat(line.getSections().getBottomStation()).isEqualTo(station);
    }
}
