package subway.application.line.port.in;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LineResponseDto {

    private Long id;
    private String name;
    private String color;
    private List<InterStationResponseDto> interStations;
}
