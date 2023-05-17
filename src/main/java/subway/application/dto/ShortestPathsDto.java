package subway.application.dto;

import java.util.ArrayList;
import java.util.List;
import subway.domain.path.PathSections;

public class ShortestPathsDto {

    private final List<PathDto> pathDtos;
    private final int totalDistance;

    private ShortestPathsDto(final List<PathDto> pathDtos, final int totalDistance) {
        this.pathDtos = pathDtos;
        this.totalDistance = totalDistance;
    }

    public static ShortestPathsDto from(final List<PathSections> shortestPathSections) {
        final List<PathDto> pathDtos = new ArrayList<>();
        int totalDistance = 0;

        for (PathSections pathSections : shortestPathSections) {
            final PathDto pathDto = PathDto.from(pathSections);
            pathDtos.add(pathDto);
            totalDistance += pathDto.getPathDistance();
        }

        return new ShortestPathsDto(pathDtos, totalDistance);
    }

    public List<PathDto> getPathDtos() {
        return pathDtos;
    }

    public int getTotalDistance() {
        return totalDistance;
    }
}
