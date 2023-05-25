package subway.application;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Fare;
import subway.domain.Line;
import subway.domain.PathFinder;
import subway.domain.Section;
import subway.domain.Station;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;
import subway.ui.dto.request.PathRequest;
import subway.ui.dto.request.SectionCreateRequest;
import subway.ui.dto.request.SectionDeleteRequest;
import subway.ui.dto.response.PathResponse;
import subway.ui.dto.response.StationInfo;

@Transactional(readOnly = true)
@Service
public class SectionService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public SectionService(
            final LineRepository lineRepository,
            final SectionRepository sectionRepository,
            final StationRepository stationRepository
    ) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public void createSection(final SectionCreateRequest request) {
        Station leftStation = findStation(request.getLeftStationName());
        Station rightStation = findStation(request.getRightStationName());
        Section section = new Section(leftStation, rightStation, request.getDistance());
        Line line = findLine(request.getLineId());

        line.addSection(section);
        sectionRepository.save(line);
    }

    public PathResponse findPath(final PathRequest request) {
        Station fromStation = findStation(request.getFromStationName());
        Station toStation = findStation(request.getToStationName());

        PathFinder pathFinder = new PathFinder(getAllSections());
        int pathDistance = pathFinder.getPathDistance(fromStation, toStation);
        List<StationInfo> stationInfos = getStationInfos(fromStation, toStation, pathFinder);
        int fare = Fare.from(pathDistance).getValue();

        return new PathResponse(fare, pathDistance, stationInfos);
    }

    private List<StationInfo> getStationInfos(Station fromStation, Station toStation, PathFinder pathFinder) {
        return pathFinder.getStations(fromStation, toStation)
                .stream()
                .map(station -> new StationInfo(station.getName()))
                .collect(Collectors.toList());
    }

    private List<LinkedList<Section>> getAllSections() {
        return lineRepository.findAll()
                .stream()
                .map(Line::getSections)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSection(final SectionDeleteRequest request) {
        Station station = findStation(request.getStationName());
        Line line = findLine(request.getLineId());

        line.deleteSection(station);
        sectionRepository.save(line);
    }

    private Line findLine(final Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("노선이 존재하지 않습니다."));
    }

    private Station findStation(final String name) {
        return stationRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 역이 없습니다."));
    }
}
