package subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubwayMapTest {
    Line line;
    SubwayMap subwayMap;

    @BeforeEach
    void init() {
        String name = "강남역";
        Station downEndStation = new Station("역삼역");
        Distance distance = new Distance(10);
        Station upEndStation = new Station(name, downEndStation, distance);
        line = new Line("2호선", "초록색", upEndStation);

        subwayMap = new SubwayMap(new ArrayList<>(List.of(line)));
    }

    @Test
    @DisplayName("addLine()을 호출했을 때 추가하려는 노선과 같은 이름의 노선이 이미 존재한다면 예외 처리")
    void addLine_fail() {
        //given, when, then
        assertThatThrownBy(() -> subwayMap.addLine(line))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("같은 이름의 노선을 추가할 수 없습니다.");
    }

    @Test
    @DisplayName("addLine()을 호출했을 때 추가하려는 노선이 올바르다면 노선이 정상적으로 추가된다.")
    void addLine_success() {
        //given
        String name = "강남역";
        Station downEndStation = new Station("역삼역");
        Distance distance = new Distance(10);
        Station upEndStation = new Station(name, downEndStation, distance);
        Line newLine = new Line("3호선", "초록색", upEndStation);

        //when
        subwayMap.addLine(newLine);

        //then
        assertThat(subwayMap).extracting("lines").asList().hasSize(2);
    }

}
