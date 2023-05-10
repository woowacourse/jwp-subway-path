package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    @DisplayName("추가하려는 역이 이미 존재하는 경우 노선에 역을 추가할 수 없다.")
    @Test
    void shouldThrowExceptionWhenInputStationAlreadyExist() {
        Line line = Line.of("2호선", "잠실역", "몽촌토성역", 5);

        assertThatThrownBy(() -> line.addStation("잠실역", "몽촌토성역", Direction.UPWARD, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 노선에 존재하는 역은 추가할 수 없습니다." + System.lineSeparator() +
                        "추가하려는 노선 : 2호선" + System.lineSeparator() +
                        "추가하려는 역 : 잠실역");
    }

    @DisplayName("이웃 역 기준 상행 방향에 역을 추가한다.")
    @Test
    void shouldAddSectionAlongUpwardWhenInputStationToAdd() {
        Line line = Line.of("2호선", "잠실역", "몽촌토성역", 5);
        line.addStation("강남역", "몽촌토성역", Direction.UPWARD, 2);

        assertThat(line.getSections()).hasSize(2);
    }

    @DisplayName("이웃 역 기준 하행 방향에 역을 추가한다.")
    @Test
    void shouldAddSectionAlongDownwardWhenInputStationToAdd() {
        Line line = Line.of("2호선", "잠실역", "몽촌토성역", 5);
        line.addStation("강남역", "잠실역", Direction.DOWNWARD, 2);

        assertThat(line.getSections()).hasSize(2);
    }

    @DisplayName("상행 종점에 역을 추가한다.")
    @Test
    void shouldAddUpwardTerminusWhenInputStationToAdd() {
        Line line = Line.of("2호선", "잠실역", "몽촌토성역", 5);
        line.addStation("까치산역", "잠실역", Direction.UPWARD, 2);

        assertThat(line.getSections()).hasSize(2);
    }

    @DisplayName("하행 종점에 역을 추가한다.")
    @Test
    void shouldAddDownwardTerminusWhenInputStationToAdd() {
        Line line = Line.of("2호선", "잠실역", "몽촌토성역", 5);
        line.addStation("까치산역", "몽촌토성역", Direction.DOWNWARD, 2);

        assertThat(line.getSections()).hasSize(2);
    }
}
