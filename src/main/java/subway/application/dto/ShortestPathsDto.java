package subway.application.dto;

import java.util.ArrayList;
import java.util.List;
import subway.domain.path.graph.PathEdges;

public class ShortestPathsDto {

    private final List<PathDto> pathDtos;
    private final int totalDistance;

    private ShortestPathsDto(final List<PathDto> pathDtos, final int totalDistance) {
        this.pathDtos = pathDtos;
        this.totalDistance = totalDistance;
    }

    public static ShortestPathsDto from(final List<PathEdges> shortestPathSections) {
        final List<PathDto> pathDtos = new ArrayList<>();
        int totalDistance = 0;

        for (PathEdges pathEdges : shortestPathSections) {
            final PathDto pathDto = PathDto.from(pathEdges);
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
