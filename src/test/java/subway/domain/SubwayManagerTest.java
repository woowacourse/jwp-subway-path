package subway.domain;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SubwayManagerTest {

    private SubwayManager subwayManager;

    @BeforeEach
    void setUp() {
        MyLines myLines = new MyLines(
                List.of(
                        MyLine.createLine("1호선", new MyStation("잠실"), new MyStation("잠실나루"), 3),
                        MyLine.createLine("2호선", new MyStation("abc"), new MyStation("def"), 5)
                )
        );

        subwayManager = new SubwayManager(myLines);
    }

    @Test
    @DisplayName("기존 노선에 새로운 역을 추가한다")
    void addStationToExistLine() {
        // given
        subwayManager.addStationToExistLine("1호선", "잠실나루", "잠실새내", 7);

        // when
        List<MyStation> allStation = subwayManager.findAllStation("1호선");

        // then
        Assertions.assertThat(allStation).containsExactly(
                new MyStation("잠실"),
                new MyStation("잠실나루"),
                new MyStation("잠실새내")
        );
    }
}
