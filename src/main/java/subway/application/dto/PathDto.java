package subway.application.dto;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import subway.domain.path.PathSection;
import subway.domain.path.PathSections;
import subway.domain.station.Station;

public class PathDto {

    private final List<ReadStationDto> stations;
    private final int pathDistance;

    public PathDto(final List<ReadStationDto> stations, final int pathDistance) {
        this.stations = stations;
        this.pathDistance = pathDistance;
    }

    public static PathDto from(final PathSections pathSections) {
        final Set<Station> uniqueOrderedStation = new LinkedHashSet<>();

        for (PathSection pathSection : pathSections.getPathSections()) {
            uniqueOrderedStation.add(pathSection.sourceStation());
            uniqueOrderedStation.add(pathSection.targetStation());
        }

        final List<ReadStationDto> readStationDtos = uniqueOrderedStation.stream()
                .map(ReadStationDto::from)
                .collect(Collectors.toList());

        return new PathDto(readStationDtos, pathSections.calculateSectionDistance());
    }

    public List<ReadStationDto> getStations() {
        return stations;
    }

    public int getPathDistance() {
        return pathDistance;
    }
}
