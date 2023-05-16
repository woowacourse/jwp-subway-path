package subway.application.line.port.in;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.domain.line.Line;

@Getter
@AllArgsConstructor
public class LineResponseDto {

    private Long id;
    private String name;
    private String color;
    private List<InterStationResponseDto> interStations;

    public static LineResponseDto from(final Line savedLine) {
        return new LineResponseDto(savedLine.getId(), savedLine.getName().getValue(),
            savedLine.getColor().getValue(),
            InterStationResponseDto.from(savedLine.getInterStations().getInterStations()));
    }
}
