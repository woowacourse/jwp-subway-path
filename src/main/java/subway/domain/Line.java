package subway.domain;

import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Line {

    private final String color;
    private final List<InterStation> interStations;
}
