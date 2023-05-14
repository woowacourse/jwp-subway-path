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
    void testAddTopStationWhenEmpty() {
        //given
        final Sections sections = new Sections(Collections.emptyList());
        final Line line = new Line("name", "color", sections);
        final Station station = new Station("station");
        final long distance = 10L;

        //when
        //then
        assertThatThrownBy(() -> line.addTopStation(station, distance))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("비어있지 않은 호선의 맨 위에 역을 추가한다.")
    void testAddTopStationWhenNotEmpty() {
        //given
        final Station firstStation = new Station("firstStation");
        final Station secondStation = new Station("secondStation");
        final Station thirdStation = new Station("thirdStation");
        final long distance = 10L;
        final Section section1 = new Section(firstStation, secondStation, distance);
        final Section section2 = new Section(secondStation, thirdStation, distance);
        final Sections sections = new Sections(new ArrayList<>(List.of(section1, section2)));
        final Line line = new Line("name", "color", sections);
        final Station station = new Station("station");

        //when
        line.addTopStation(station, distance);

        //then
        assertThat(line.getStationsSize()).isEqualTo(4);
        assertThat(line.getSections().findTopStation()).isEqualTo(station);
    }

    @Test
    @DisplayName("빈 호선의 맨 아래에 역을 추가한다.")
    void testAddBottomStationWhenEmpty() {
        //given
        final Sections sections = new Sections(Collections.emptyList());
        final Line line = new Line("name", "color", sections);
        final Station station = new Station("station");
        final long distance = 10L;

        //when
        //then
        assertThatThrownBy(() -> line.addBottomStation(station, distance))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("비어있지 않은 호선의 맨 아래에 역을 추가한다.")
    void testAddBottomStationWhenNotEmpty() {
        //given
        final Station firstStation = new Station("firstStation");
        final Station secondStation = new Station("secondStation");
        final Station thirdStation = new Station("thirdStation");
        final long distance = 10L;
        final Section section1 = new Section(firstStation, secondStation, distance);
        final Section section2 = new Section(secondStation, thirdStation, distance);
        final Sections sections = new Sections(new ArrayList<>(List.of(section1, section2)));
        final Line line = new Line("name", "color", sections);
        final Station station = new Station("station");

        //when
        line.addBottomStation(station, distance);

        //then
        assertThat(line.getStationsSize()).isEqualTo(4);
        assertThat(line.getSections().findBottomStation()).isEqualTo(station);
    }

    @Test
    @DisplayName("빈 호선의 사이에 역을 추가한다.")
    void testAddBetweenStationWhenEmpty() {
        //given
        final Sections sections = new Sections(Collections.emptyList());
        final Line line = new Line("name", "color", sections);
        final Station station1 = new Station("station1");
        final Station station2 = new Station("station2");
        final Station station = new Station("station");
        final long distance = 10L;

        //when
        //then
        assertThatThrownBy(() -> line.addBetweenStation(station, station1, station2, 10L))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("비어있지 않은 호선의 사이에에 역을 추가한다.")
    void testAddBetweenStationWhenNotEmpty() {
        //given
        final Station firstStation = new Station("firstStation");
        final Station secondStation = new Station("secondStation");
        final Station thirdStation = new Station("thirdStation");
        final long distance = 10L;
        final Section section1 = new Section(firstStation, secondStation, distance);
        final Section section2 = new Section(secondStation, thirdStation, distance);
        final Sections sections = new Sections(new ArrayList<>(List.of(section1, section2)));
        final Line line = new Line("name", "color", sections);
        final Station station = new Station("station");

        //when
        line.addBetweenStation(station, firstStation, secondStation, distance);

        //then
        assertThat(line.getStationsSize()).isEqualTo(4);
        assertThat(line.getSections().findStation(1)).isEqualTo(station);
    }

    @Test
    @DisplayName("빈 호선에 station을 초기화한다.")
    void testAddInitStations() {
        //given
        final Station upStation = new Station("upStation");
        final Station downStation = new Station("downStation");
        final long distance = 10L;
        final Sections sections = new Sections(new ArrayList<>());
        final Line line = new Line("name", "color", sections);

        //when
        line.addInitStations(upStation, downStation, distance);

        //then
        assertThat(line.getStationsSize()).isEqualTo(2);
        assertThat(line.getSections().findStation(0)).isEqualTo(upStation);
    }

    @Test
    @DisplayName("비어있지 않은 호선에 station을 초기화한다.")
    void testAddInitStationsWhenLineNotEmpty() {
        //given
        final Station upStation = new Station("upStation");
        final Station downStation = new Station("downStation");
        final long distance = 10L;
        final Section section = new Section(upStation, downStation, distance);
        final Sections sections = new Sections(new ArrayList<>(List.of(section)));
        final Line line = new Line("name", "color", sections);

        //when
        //then
        assertThatThrownBy(() -> line.addInitStations(upStation, downStation, distance))
            .isInstanceOf(BusinessException.class);
    }
}
