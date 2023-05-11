package subway.domain;

import org.assertj.core.api.Assertions;
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
        line = new Line(1L, "2호선", "초록색", upEndStation);

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
        Line newLine = new Line(1L, "3호선", "초록색", upEndStation);

        //when
        subwayMap.addLine(newLine);

        //then
        assertThat(subwayMap).extracting("lines").asList().hasSize(2);
    }

    @Test
    @DisplayName("removeLine()으로 노선을 삭제할 수 있다")
    void removeLine_success() {
        //given
        String name = "강남역";
        Station downEndStation = new Station("역삼역");
        Distance distance = new Distance(10);
        Station upEndStation = new Station(name, downEndStation, distance);
        Line newLine = new Line(1L, "3호선", "초록색", upEndStation);

        SubwayMap subwayMap = new SubwayMap(new ArrayList<>(List.of(line, newLine)));

        //when
        subwayMap.removeLine("3호선");

        //then
        Assertions.assertThat(subwayMap)
                .extracting("lines")
                .asList()
                .hasSize(1);
    }

    @Test
    @DisplayName("removeLine()으로 올바르지 않은 노선의 이름이 입력되면 오류 발생")
    void removeLine_fail() {
        //given
        String name = "강남역";
        Station downEndStation = new Station("역삼역");
        Distance distance = new Distance(10);
        Station upEndStation = new Station(name, downEndStation, distance);
        Line newLine = new Line(1L, "3호선", "초록색", upEndStation);

        SubwayMap subwayMap = new SubwayMap(List.of(line, newLine));

        //when, then
        Assertions.assertThatThrownBy(() -> subwayMap.removeLine("4호선"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선도에 존재하지 않는 노선입니다");
    }

}
