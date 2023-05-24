package subway.application.dto;

import java.util.ArrayList;
import java.util.List;
import subway.domain.path.Path;
import subway.domain.path.PathSections;

public class ShortestPathsDto {

    private final List<PathDto> pathDtos;
    private final int totalDistance;

    private ShortestPathsDto(final List<PathDto> pathDtos, final int totalDistance) {
        this.pathDtos = pathDtos;
        this.totalDistance = totalDistance;
    }

    public static ShortestPathsDto from(final Path path) {
        final List<PathDto> pathDtos = new ArrayList<>();

        for (PathSections pathSections : path.getPathSections()) {
            final PathDto pathDto = PathDto.from(pathSections);
            pathDtos.add(pathDto);
        }

        return new ShortestPathsDto(pathDtos, path.calculateTotalDistance());
    }

    public List<PathDto> getPathDtos() {
        return pathDtos;
    }

    public int getTotalDistance() {
        return totalDistance;
    }
}
