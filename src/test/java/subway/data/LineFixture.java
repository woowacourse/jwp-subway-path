package subway.data;

import java.util.List;
import subway.domain.line.Line;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.Stations;

public class LineFixture {

    public final static Line LINE_2 = new Line(0L, "2호선", "#FFFFFF", new Stations(List.of()), new Sections(List.of()));
}
