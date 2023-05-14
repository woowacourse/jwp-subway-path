package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.BusinessException;

class LineTest {

    private final Station firstStation = new Station("firstStation");
    private final Station secondStation = new Station("secondStation");
    private final Station thirdStation = new Station("thirdStation");
    private final long distance = 10L;
    private final Section section1 = new Section(firstStation, secondStation, distance);
    private final Section section2 = new Section(secondStation, thirdStation, distance);

    @Test
    @DisplayName("빈 호선의 맨 위에 역을 추가한다.")
    void testAddTopStationWhenEmpty() {
        //given
        final Sections sections = new Sections(Collections.emptyList());
        final Line line = new Line("name", "color", sections);

        //when
        //then
        assertThatThrownBy(() -> line.addTopStation(firstStation, distance))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("비어있지 않은 호선의 맨 위에 역을 추가한다.")
    void testAddTopStationWhenNotEmpty() {
        //given
        final Sections sections = new Sections(new ArrayList<>(List.of(section1, section2)));
        final Line line = new Line("name", "color", sections);

        //when
        line.addTopStation(thirdStation, distance);

        //then
        assertThat(line.getStationsSize()).isEqualTo(4);
        assertThat(line.getSections().findTopStation()).isEqualTo(thirdStation);
    }

    @Test
    @DisplayName("빈 호선의 맨 아래에 역을 추가한다.")
    void testAddBottomStationWhenEmpty() {
        //given
        final Sections sections = new Sections(Collections.emptyList());
        final Line line = new Line("name", "color", sections);
        final long distance = 10L;

        //when
        //then
        assertThatThrownBy(() -> line.addBottomStation(firstStation, distance))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("비어있지 않은 호선의 맨 아래에 역을 추가한다.")
    void testAddBottomStationWhenNotEmpty() {
        //given
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

        //when
        //then
        assertThatThrownBy(() -> line.addBetweenStation(firstStation, secondStation, thirdStation, distance))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("비어있지 않은 호선의 사이에에 역을 추가한다.")
    void testAddBetweenStationWhenNotEmpty() {
        //given
        final Sections sections = new Sections(new ArrayList<>(List.of(section1, section2)));
        final Line line = new Line("name", "color", sections);
        final Station station = new Station("station");

        //when
        line.addBetweenStation(station, firstStation, secondStation, 5L);

        //then
        assertThat(line.getStationsSize()).isEqualTo(4);
        assertThat(line.getSections().findStation(1)).isEqualTo(station);
    }

    @Test
    @DisplayName("빈 호선에 station을 초기화한다.")
    void testAddInitStations() {
        //given
        final Sections sections = new Sections(new ArrayList<>());
        final Line line = new Line("name", "color", sections);

        //when
        line.addInitStations(firstStation, secondStation, distance);

        //then
        assertThat(line.getStationsSize()).isEqualTo(2);
        assertThat(line.getSections().findStation(0)).isEqualTo(firstStation);
    }

    @Test
    @DisplayName("비어있지 않은 호선에 station을 초기화한다.")
    void testAddInitStationsWhenLineNotEmpty() {
        //given
        final Section section = new Section(firstStation, secondStation, distance);
        final Sections sections = new Sections(new ArrayList<>(List.of(section)));
        final Line line = new Line("name", "color", sections);

        //when
        //then
        assertThatThrownBy(() -> line.addInitStations(firstStation, secondStation, distance))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("호선에서 맨 위에 있는 역을 삭제한다.")
    void testRemoveStationTop() {
        //given
        final Sections sections = new Sections(new ArrayList<>(List.of(section1, section2)));
        final Line line = new Line("name", "color", sections);

        //when
        line.removeStation(firstStation);

        //then
        final Sections removedSections = line.getSections();
        assertThat(removedSections.getStationsSize()).isEqualTo(2);
        assertThat(removedSections.findTopStation()).isEqualTo(secondStation);
        assertThat(removedSections.findBottomStation()).isEqualTo(thirdStation);
    }

    @Test
    @DisplayName("호선에서 맨 아래에 있는 역을 삭제한다.")
    void testRemoveStationBottom() {
        //given
        final Sections sections = new Sections(new ArrayList<>(List.of(section1, section2)));
        final Line line = new Line("name", "color", sections);

        //when
        line.removeStation(thirdStation);

        //then
        final Sections removedSections = line.getSections();
        assertThat(removedSections.getStationsSize()).isEqualTo(2);
        assertThat(removedSections.findTopStation()).isEqualTo(firstStation);
        assertThat(removedSections.findBottomStation()).isEqualTo(secondStation);
    }

    @Test
    @DisplayName("호선에서 중간에 있는 역을 삭제한다.")
    void testRemoveStationBetween() {
        //given
        final Sections sections = new Sections(new ArrayList<>(List.of(section1, section2)));
        final Line line = new Line("name", "color", sections);

        //when
        line.removeStation(secondStation);

        //then
        final Sections removedSections = line.getSections();
        assertThat(removedSections.getStationsSize()).isEqualTo(2);
        assertThat(removedSections.findTopStation()).isEqualTo(firstStation);
        assertThat(removedSections.findBottomStation()).isEqualTo(thirdStation);
    }
}
