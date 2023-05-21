package subway.domain.path;

import java.util.ArrayList;
import java.util.List;

import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.dto.PathSectionDto;
import subway.dto.StationFindDto;

public class PathSections {

    private final List<Section> orderedSections;

    public PathSections(List<Section> orderedSections) {
        this.orderedSections = orderedSections;
    }

    public List<PathSectionDto> toPathSectionDtos() {
        List<PathSectionDto> pathSectionDtos = new ArrayList<>();
        for (Section section : orderedSections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            StationFindDto currentStation = new StationFindDto(upStation.getLine().getName(), upStation.getName());
            StationFindDto nextStation = new StationFindDto(downStation.getLine().getName(), downStation.getName());
            boolean isTransferSection = section.isTransferSection();

            PathSectionDto pathSectionDto =
                    new PathSectionDto(currentStation, nextStation, isTransferSection, section.getDistance());
            pathSectionDtos.add(pathSectionDto);
        }
        return pathSectionDtos;
    }

    public int getTransferCount() {
        return (int) orderedSections.stream()
                .filter(Section::isTransferSection)
                .count();
    }

    public int getTotalDistance() {
        return orderedSections.stream()
                .map(Section::getDistance)
                .reduce(0, Integer::sum);
    }
}
