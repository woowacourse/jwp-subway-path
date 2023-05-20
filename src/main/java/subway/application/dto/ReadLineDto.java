package subway.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.line.Line;

public class ReadLineDto {

    private final Long id;
    private final String name;
    private final String color;
    private final List<ReadStationDto> stationDtos;

    private ReadLineDto(
            final Long id,
            final String name,
            final String color,
            final List<ReadStationDto> stationDtos
    ) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationDtos = stationDtos;
    }

    public static ReadLineDto from(final Line line) {
        final List<ReadStationDto> stationDtos = line.findStationsByOrdered()
                .stream()
                .map(ReadStationDto::from)
                .collect(Collectors.toList());

        return new ReadLineDto(line.getId(), line.getName(), line.getColor(), stationDtos);
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

    public List<ReadStationDto> getStationDtos() {
        return stationDtos;
    }
}