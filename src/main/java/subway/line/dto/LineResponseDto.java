package subway.line.dto;

import java.util.List;

public class LineResponseDto {

    private final Long id;
    private final String name;
    private final List<StationResponseDto> stationResponseDtos;

    public LineResponseDto(final Long id, final String name, final List<StationResponseDto> stationResponseDtos) {
        this.id = id;
        this.name = name;
        this.stationResponseDtos = stationResponseDtos;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StationResponseDto> getStationResponseDtos() {
        return stationResponseDtos;
    }
}
