package subway.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LineFixture {
    public static List<Station> findAllStationsInLineFixtures() {
        return new ArrayList<>(Arrays.asList(
                new Station(1L, "잠실"),
                new Station(2L, "건대"),
                new Station(3L, "동대문역사문화공원"),
                new Station(4L, "혜화")
        ));
    }

    public static List<Line> findAllLineFixtures() {
        return new ArrayList<>(Arrays.asList(
                new Line(1L, "2호선", "초록색",
                        StationEdges.from(List.of(
                                new StationEdge(1L, 0),
                                new StationEdge(2L, 5),
                                new StationEdge(3L, 5)
                        ))),
                new Line(2L, "4호선", "파란색",
                        StationEdges.from(List.of(
                                new StationEdge(3L, 0),
                                new StationEdge(4L, 6)
                        )))
        ));
    }
}
