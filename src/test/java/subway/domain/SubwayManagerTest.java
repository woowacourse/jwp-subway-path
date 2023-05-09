package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SubwayManagerTest {

    private SubwayManager subwayManager;

    @BeforeEach
    void setUp() {
        List<MyLine> lines = List.of(
                MyLine.createLine("1호선", new MyStation("잠실"), new MyStation("잠실나루"), 3),
                MyLine.createLine("2호선", new MyStation("abc"), new MyStation("def"), 5)
        );

        MyLines myLines = new MyLines(new ArrayList<>(lines));

        subwayManager = new SubwayManager(myLines);
    }

    @DisplayName("새로운 노선을 추가한다")
    @Test
    void createNewLine() {
        // given
        subwayManager.createNewLine("3호선", "안국", "경복궁", 3);

        // when
        List<MyStation> allStation = subwayManager.findAllStation("3호선");

        // then
        assertThat(allStation).containsExactly(
                new MyStation("안국"),
                new MyStation("경복궁")
        );
    }

    @Test
    @DisplayName("기존 노선에 새로운 역을 추가한다")
    void addStationToExistLine() {
        // given
        subwayManager.addStationToExistLine("1호선", "잠실나루", "잠실새내", 7);

        // when
        List<MyStation> allStation = subwayManager.findAllStation("1호선");

        // then
        assertThat(allStation).containsExactly(
                new MyStation("잠실"),
                new MyStation("잠실나루"),
                new MyStation("잠실새내")
        );
    }
}
