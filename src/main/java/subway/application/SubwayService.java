package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.LineName;
import subway.domain.Lines;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.Stations;
import subway.domain.path.ShortestPath;
import subway.domain.path.ShortestPathFinder;
import subway.dto.AddLineRequest;
import subway.dto.AddStationRequest;
import subway.dto.DeleteStationRequest;
import subway.dto.SubwayPathRequest;
import subway.repository.SubwayRepository;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class SubwayService {

    private final SubwayRepository subwayRepository;

    public SubwayService(SubwayRepository subwayRepository) {
        this.subwayRepository = subwayRepository;
    }

    public long addStation(AddStationRequest addStationRequest) {
        Line line = subwayRepository.findLineById(addStationRequest.getLineId());
        Station stationToAdd = findStationByName(addStationRequest.getAddStationName()).orElseGet(() -> createNewStation(addStationRequest.getAddStationName()));
        Station upstream = findStationById(addStationRequest.getUpstreamId());
        Station downstream = findStationById(addStationRequest.getDownstreamId());
        line.addStation(stationToAdd, upstream, downstream, addStationRequest.getDistanceToUpstream());

        return subwayRepository.updateLine(line);
    }

    private Optional<Station> findStationByName(String stationName) {
        Stations stations = subwayRepository.getStations();
        return stations.findStationByName(stationName);
    }

    public Station findStationById(long stationId) {
        Stations stations = subwayRepository.getStations();
        return stations.findStationById(stationId);
    }

    private Station createNewStation(String stationName) {
        Station newStation = new Station(stationName);
        long stationId = subwayRepository.addStation(newStation);
        return new Station(stationId, newStation.getName());
    }

    public void deleteStation(DeleteStationRequest deleteStationRequest) {
        Line line = subwayRepository.findLineById(deleteStationRequest.getLineId());
        Station stationToDelete = findStationById(deleteStationRequest.getStationId());
        line.deleteStation(stationToDelete);
        subwayRepository.updateLine(line);
    }

    public long addNewLine(AddLineRequest addLineRequest) {
        LineName lineNameToAdd = new LineName(addLineRequest.getLineName());
        Station upstream = findStationByName(addLineRequest.getUpstreamName()).orElseGet(() -> createNewStation(addLineRequest.getUpstreamName()));
        Station downstream = findStationByName(addLineRequest.getDownstreamName()).orElseGet(() -> createNewStation(addLineRequest.getDownstreamName()));
        Section section = new Section(upstream, downstream, addLineRequest.getDistance());
        Line newLine = new Line(lineNameToAdd, section);
        Lines lines = subwayRepository.getLines();
        lines.addNewLine(newLine);
        return subwayRepository.addNewLine(newLine);
    }

    public Line findLineById(long id) {
        return subwayRepository.findLineById(id);
    }

    public List<Line> findAllLines() {
        return subwayRepository.getLines().getLines();
    }

    public ShortestPath findShortestPath(SubwayPathRequest subwayPathRequest) {
        ShortestPathFinder shortestPathFinder = new ShortestPathFinder();
        return shortestPathFinder.findShortestPath(
                subwayRepository.getSections(),
                subwayRepository.getStations(),
                subwayRepository.findStationById(subwayPathRequest.getDepartureId()),
                subwayRepository.findStationById(subwayPathRequest.getDestinationId())
        );
    }

    @Override
    public String toString() {
        return "SubwayService{" +
                "subwayRepository=" + subwayRepository +
                '}';
    }
}
