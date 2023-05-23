package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.PathDto;
import subway.application.dto.StationDto;
import subway.domain.*;
import subway.domain.repository.SectionRepository;
import subway.domain.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class PathService {
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public PathService(StationRepository stationRepository, SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public PathDto findShortestPath(Long startStationId, Long endStationId) {
        Path path = createPath();

        Station startStation = stationRepository.findById(startStationId);
        Station endStation = stationRepository.findById(endStationId);

        List<Station> pathOfStations = path.getShortestPath(startStation, endStation);
        Distance distance = path.getShortestDistance(startStation, endStation);

        return toPathDto(pathOfStations, distance);
    }

    private Path createPath() {
        List<Station> stations = stationRepository.findAll();
        List<Section> sections = sectionRepository.findAll();

        return new Path(stations, sections);
    }

    private PathDto toPathDto(List<Station> stations, Distance distance) {
        List<StationDto> stationDtos = stations.stream()
                .map(this::toStationDto)
                .collect(Collectors.toList());
        Cost cost = new Cost(distance);

        return new PathDto(stationDtos, cost.getCost());
    }

    private StationDto toStationDto(Station station) {
        Long stationId = stationRepository.findIdByName(station.getName());

        return new StationDto(stationId, station.getName());
    }
}
