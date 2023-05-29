package subway.business.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.fixtures.station.StationFixture.*;

class LineTest {
    @DisplayName("추가하려는 역이 이미 존재하는 경우 예외가 발생한다.")
    @Test
    void shouldThrowExceptionWhenInputStationToAddAlreadyExist() {
        Line line = Line.of("2호선", 강남역, 잠실역, 5, 0);

        assertThatThrownBy(() -> line.addStation(잠실역, 강남역, Direction.UPWARD, 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 노선에 존재하는 역은 추가할 수 없습니다. " +
                        "(추가하려는 노선 : 2호선 추가하려는 역 : 잠실역)");
    }

    @DisplayName("이웃역이 이미 존재하는 경우 예외가 발생한다.")
    @Test
    void shouldThrowExceptionWhenInputNeighborhoodStationAlreadyExist() {
        Line line = Line.of("2호선", 강남역, 잠실역, 5);

        assertThatThrownBy(() -> line.addStation(성수역, 역삼역, Direction.UPWARD, 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("추가하려는 역의 이웃 역이 존재하지 않습니다. " +
                        "(추가하려는 노선 : 2호선 존재하지 않는 이웃 역 : 역삼역)");
    }

    @DisplayName("저장하려는 위치의 구간 거리보다 입력한 거리가 더 큰 경우 예외가 발생한다.")
    @Test
    void shouldThrowExceptionWhenDistanceToSaveIsSameOrOverExistingDistance() {
        Line line = Line.of("2호선", 강남역, 잠실역, 5);

        assertThatThrownBy(() -> line.addStation(성수역, 잠실역, Direction.UPWARD, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("저장하려는 위치의 구간 거리보다, 입력한 거리가 더 크거나 같습니다. " +
                        "(입력한 거리 : 5 저장하려는 위치의 구간 거리 : 5)");
    }

    @DisplayName("이웃 역 기준 상행 방향에 역을 추가한다.")
    @Test
    void shouldAddSectionAlongUpwardWhenInputStationToAdd() {
        Line line = Line.of("2호선", 강남역, 잠실역, 5);
        line.addStation(역삼역, 잠실역, Direction.UPWARD, 2);

        // 강남역(상행) - 역삼역 - 잠실역(하행)
        assertThat(line.getSections()).hasSize(2);
        assertThat(line.getSections().get(1).getUpwardStation().getName()).isEqualTo("역삼역");
    }

    @DisplayName("이웃 역 기준 하행 방향에 역을 추가한다.")
    @Test
    void shouldAddSectionAlongDownwardWhenInputStationToAdd() {
        Line line = Line.of("2호선", 강남역, 잠실역, 5);
        line.addStation(역삼역, 강남역, Direction.DOWNWARD, 2);

        assertThat(line.getSections()).hasSize(2);
        assertThat(line.getSections().get(0).getDownwardStation().getName()).isEqualTo("역삼역");
    }

    @DisplayName("상행 종점에 역을 추가한다.")
    @Test
    void shouldAddUpwardTerminusWhenInputStationToAdd() {
        Line line = Line.of("2호선", 강남역, 잠실역, 5);
        line.addStation(성수역, 강남역, Direction.UPWARD, 2);

        assertThat(line.getSections()).hasSize(2);
        assertThat(line.getSections().get(0).getUpwardStation().getName()).isEqualTo("성수역");
    }

    @DisplayName("하행 종점에 역을 추가한다.")
    @Test
    void shouldAddDownwardTerminusWhenInputStationToAdd() {
        Line line = Line.of("2호선", 강남역, 잠실역, 5);
        line.addStation(성수역, 잠실역, Direction.DOWNWARD, 2);

        assertThat(line.getSections()).hasSize(2);
        assertThat(line.getSections().get(1).getDownwardStation().getName()).isEqualTo("성수역");
    }

    @DisplayName("삭제하려는 역이 노선에 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void shouldThrowExceptionWhenInputStationToDeleteAlreadyExist() {
        Line line = Line.of("2호선", 강남역, 잠실역, 5);
        line.addStation(역삼역, 잠실역, Direction.DOWNWARD, 2);
        // 현재 노선 상태 : (상행) 강남역 - 역삼역 - 잠실역 (하행)

        assertThatThrownBy(() -> line.deleteStation(성수역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("삭제하려는 역이 노선에 존재하지 않습니다. " +
                        "(삭제하려는 역 : 성수역)");
    }

    @DisplayName("역 삭제 시, 노선에 두 개의 역만 존재하는 경우 예외가 발생한다.")
    @Test
    void shouldThrowExceptionWhenDeleteStationFromLineHaveOnly2Stations() {
        Line line = Line.of("2호선", 강남역, 잠실역, 5);

        assertThatThrownBy(() -> line.deleteStation(잠실역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선에 역이 2개 밖에 존재하지 않아, 역을 제외할 수 없습니다.");
    }

    @DisplayName("노선의 가운데에 있는 역을 삭제한다.")
    @Test
    void shouldDeleteStationWhenInputStationInMiddle() {
        Line line = Line.of("2호선", 강남역, 잠실역, 5);
        line.addStation(역삼역, 강남역, Direction.DOWNWARD, 2);
        // 현재 노선 상태 : (상행) 강남역 - 역삼역 - 잠실역 (하행)

        line.deleteStation(역삼역);
        // 현재 노선 상태 : (상행) 강남역 - 잠실역 (하행)

        assertThat(line.getSections()).hasSize(1);
        assertThat(line.getSections().get(0).getDownwardStation().getName()).isEqualTo("잠실역");
    }

    @DisplayName("상행 종점에 있는 역을 삭제한다.")
    @Test
    void shouldDeleteStationWhenInputStationIsUpwardTerminus() {
        Line line = Line.of("2호선", 강남역, 잠실역, 5);
        line.addStation(역삼역, 강남역, Direction.DOWNWARD, 2);
        // 현재 노선 상태 : (상행) 강남역 - 역삼역 - 잠실역 (하행)

        line.deleteStation(강남역);
        // 현재 노선 상태 : (상행) 역삼역 - 잠실역 (하행)

        assertThat(line.getSections()).hasSize(1);
        assertThat(line.getSections().get(0).getUpwardStation().getName()).isEqualTo("역삼역");
    }

    @DisplayName("하행 종점에 있는 역을 삭제한다.")
    @Test
    void shouldDeleteStationWhenInputStationIsDownwardTerminus() {
        Line line = Line.of("2호선", 강남역, 잠실역, 5);
        line.addStation(역삼역, 강남역, Direction.DOWNWARD, 2);
        // 현재 노선 상태 : (상행) 강남역 - 역삼역 - 잠실역 (하행)

        line.deleteStation(잠실역);
        // 현재 노선 상태 : (상행) 강남역 - 역삼역 (하행)

        assertThat(line.getSections()).hasSize(1);
        assertThat(line.getSections().get(0).getDownwardStation().getName()).isEqualTo("역삼역");
    }
}
