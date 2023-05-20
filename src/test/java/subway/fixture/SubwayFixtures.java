package subway.fixture;

import java.util.List;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Subway;

public class SubwayFixtures {

    public static final Subway SUBWAY1 = new Subway(List.of(
            new Line("1호선", "RED", List.of(
                    new Section("A", "B", 5),
                    new Section("B", "C", 5)
            )),
            new Line("2호선", "BLUE", List.of(
                    new Section("Z", "B", 5),
                    new Section("B", "Y", 5)
            ))
    ));
    public static final Subway SUBWAY2 = new Subway(List.of(
            new Line("1호선", "RED",
                    List.of(
                            new Section("A", "B", 2),
                            new Section("B", "C", 3),
                            new Section("C", "D", 4)
                    )
            ),
            new Line("2호선", "BLUE",
                    List.of(
                            new Section("D", "B", 2),
                            new Section("B", "E", 2)
                    )
            )
    ));
}
