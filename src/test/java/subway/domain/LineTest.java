package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.vo.Distance;
import subway.exception.BusinessException;

class LineTest {

    private final Station firstStation = new Station("firstStation");
    private final Station secondStation = new Station("secondStation");
    private final Station thirdStation = new Station("thirdStation");
    private final Distance distance = new Distance(10L);
    private final Section section1 = new Section(firstStation, secondStation, distance);
    private final Section section2 = new Section(secondStation, thirdStation, distance);

    @Test
    @DisplayName("빈 호선의 맨 위에 역을 추가한다.")
    void testAddTopStationWhenEmpty() {
        //given
        final Sections sections = new Sections(Collections.emptyList());
        final Line line = new Line("name", "color", 0L, sections);

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
        final Line line = new Line("name", "color", 0L, sections);
        final Station newStation = new Station("newStation");

        //when
        final Line result = line.addTopStation(newStation, distance);

        //then
        assertThat(result.getStationsSize()).isEqualTo(4);
        assertThat(result.getSections().findTopStation()).isEqualTo(newStation);
    }

    @Test
    @DisplayName("빈 호선의 맨 아래에 역을 추가한다.")
    void testAddBottomStationWhenEmpty() {
        //given
        final Sections sections = new Sections(Collections.emptyList());
        final Line line = new Line("name", "color", 0L, sections);
        final Distance distance = new Distance(10L);

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
        final Line line = new Line("name", "color", 0L, sections);
        final Station newStation = new Station("newStation");

        //when
        final Line result = line.addBottomStation(newStation, distance);

        //then
        assertThat(result.getStationsSize()).isEqualTo(4);
        assertThat(result.getSections().findBottomStation()).isEqualTo(newStation);
    }

    @Test
    @DisplayName("빈 호선의 사이에 역을 추가한다.")
    void testAddBetweenStationWhenEmpty() {
        //given
        final Sections sections = new Sections(Collections.emptyList());
        final Line line = new Line("name", "color", 0L, sections);

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
        final Line line = new Line("name", "color", 0L, sections);
        final Station newStation = new Station("station");
        final Distance distance = new Distance(5L);

        //when
        final Line result = line.addBetweenStation(newStation, firstStation, secondStation, distance);

        //then
        assertThat(result.getStationsSize()).isEqualTo(4);
        assertThat(result.getSections().findStation(1)).isEqualTo(newStation);
    }

    @Test
    @DisplayName("빈 호선에 station을 초기화한다.")
    void testAddInitStations() {
        //given
        final Sections sections = new Sections(new ArrayList<>());
        final Line line = new Line("name", "color", 0L, sections);

        //when
        final Line result = line.addInitStations(firstStation, secondStation, distance);

        //then
        assertThat(result.getStationsSize()).isEqualTo(2);
        assertThat(result.getSections().findStation(0)).isEqualTo(firstStation);
    }

    @Test
    @DisplayName("비어있지 않은 호선에 station을 초기화한다.")
    void testAddInitStationsWhenLineNotEmpty() {
        //given
        final Section section = new Section(firstStation, secondStation, distance);
        final Sections sections = new Sections(new ArrayList<>(List.of(section)));
        final Line line = new Line("name", "color", 0L, sections);

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
        final Line line = new Line("name", "color", 0L, sections);

        //when
        final Line result = line.removeStation(firstStation);

        //then
        final Sections removedSections = result.getSections();
        assertThat(removedSections.getStationsSize()).isEqualTo(2);
        assertThat(removedSections.findTopStation()).isEqualTo(secondStation);
        assertThat(removedSections.findBottomStation()).isEqualTo(thirdStation);
    }

    @Test
    @DisplayName("호선에서 맨 아래에 있는 역을 삭제한다.")
    void testRemoveStationBottom() {
        //given
        final Sections sections = new Sections(new ArrayList<>(List.of(section1, section2)));
        final Line line = new Line("name", "color", 0L, sections);

        //when
        final Line result = line.removeStation(thirdStation);

        //then
        final Sections removedSections = result.getSections();
        assertThat(removedSections.getStationsSize()).isEqualTo(2);
        assertThat(removedSections.findTopStation()).isEqualTo(firstStation);
        assertThat(removedSections.findBottomStation()).isEqualTo(secondStation);
    }

    @Test
    @DisplayName("호선에서 중간에 있는 역을 삭제한다.")
    void testRemoveStationBetween() {
        //given
        final Sections sections = new Sections(new ArrayList<>(List.of(section1, section2)));
        final Line line = new Line("name", "color", 0L, sections);

        //when
        final Line result = line.removeStation(secondStation);

        //then
        final Sections removedSections = result.getSections();
        assertThat(removedSections.getStationsSize()).isEqualTo(2);
        assertThat(removedSections.findTopStation()).isEqualTo(firstStation);
        assertThat(removedSections.findBottomStation()).isEqualTo(thirdStation);
    }
}
