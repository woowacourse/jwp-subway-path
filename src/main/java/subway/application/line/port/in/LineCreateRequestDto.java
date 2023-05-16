package subway.application.line.port.in;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LineCreateRequestDto {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private long distance;
}
