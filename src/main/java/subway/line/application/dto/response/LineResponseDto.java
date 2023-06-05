package subway.line.application.dto.response;

import java.util.List;
import subway.line.domain.Line;

public class LineResponseDto {

    private final Long id;
    private final String name;
    private final String color;
    private final List<InterStationResponseDto> interStations;

    public LineResponseDto(Long id, String name, String color,
            List<InterStationResponseDto> interStations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.interStations = interStations;
    }

    public static LineResponseDto from(Line savedLine) {
        return new LineResponseDto(savedLine.getId(), savedLine.getName().getValue(),
                savedLine.getColor().getValue(),
                InterStationResponseDto.from(savedLine.getInterStations().getInterStations()));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<InterStationResponseDto> getInterStations() {
        return interStations;
    }
}
