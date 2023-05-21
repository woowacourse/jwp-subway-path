package subway.application;

import java.util.List;

import org.springframework.stereotype.Service;
import subway.domain.fare.SubwayFare;
import subway.domain.path.PathSections;
import subway.domain.path.ShortestPathFinder;
import subway.domain.section.general.GeneralSection;
import subway.domain.section.transfer.TransferSection;
import subway.domain.station.Station;
import subway.domain.station.Stations;
import subway.dto.PathResponse;
import subway.dto.PathSectionDto;
import subway.repository.GeneralSectionRepository;
import subway.repository.StationRepository;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final GeneralSectionRepository generalSectionRepository;

    public PathService(final StationRepository stationRepository, final GeneralSectionRepository generalSectionRepository) {
        this.stationRepository = stationRepository;
        this.generalSectionRepository = generalSectionRepository;
    }

    public PathResponse findShortestPath(String startLineName, String startStationName,
                                         String endLineName, String endStationName) {
        Station startStation = stationRepository.findByStationNameAndLineName(startStationName, startLineName);
        Station endStation = stationRepository.findByStationNameAndLineName(endStationName, endLineName);

        PathSections findShortestPathSections = getShortestPathSections(startStation, endStation);
        List<PathSectionDto> pathSectionDtos = findShortestPathSections.toPathSectionDtos();

        int totalDistance = findShortestPathSections.getTotalDistance();
        SubwayFare totalFare = SubwayFare.generateFareByDistance(totalDistance);

        return new PathResponse(pathSectionDtos, totalDistance, totalFare.getFare());
    }

    private PathSections getShortestPathSections(Station startStation, Station endStation) {
        List<Station> allStation = stationRepository.findAllStation();
        Stations stations = new Stations(allStation);
        List<GeneralSection> allGeneralSection = generalSectionRepository.findAll();
        List<TransferSection> allTransferSection = stations.getTransferSections();

        ShortestPathFinder shortestPathFinder = new ShortestPathFinder(allStation, allGeneralSection, allTransferSection);
        return shortestPathFinder.findShortestPathSections(startStation, endStation);
    }
}
