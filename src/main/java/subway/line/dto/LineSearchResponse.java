package subway.line.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.domain.Station;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LineSearchResponse {

    private Long id;
    private String name;
    private String color;
    private List<Station> stations;
}
