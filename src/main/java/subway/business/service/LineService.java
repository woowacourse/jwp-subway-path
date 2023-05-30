package subway.business.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.business.domain.*;
import subway.business.service.dto.*;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public void addStationToLine(long lineId, StationAddToLineRequest stationAddToLineRequest) {
        Line line = lineRepository.findById(lineId);
        Station station = stationRepository.findById(stationAddToLineRequest.getStationId());
        Station neighborhoodStation = stationRepository.findById(stationAddToLineRequest.getNeighborhoodStationId());
        line.addStation(
                station,
                neighborhoodStation,
                Direction.from(stationAddToLineRequest.getAddDirection()),
                stationAddToLineRequest.getDistance()
        );
        lineRepository.update(line);
    }

    public void deleteStation(Long lineId, StationDeleteRequest stationDeleteRequest) {
        Line line = lineRepository.findById(lineId);
        Station station = stationRepository.findById(stationDeleteRequest.getStationId());
        line.deleteStation(station);
        lineRepository.update(line);
    }

    @Transactional(readOnly = true)
    public List<LineStationsResponse> findLineResponses() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(this::getLineStationsResponseFrom)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineStationsResponse findLineResponseById(Long id) {
        Line line = lineRepository.findById(id);
        return getLineStationsResponseFrom(line);
    }

    public LineResponse createLine(LineSaveRequest lineSaveRequest) {
        Station upwardStation = stationRepository.findById(lineSaveRequest.getUpwardTerminusId());
        Station downwardStation = stationRepository.findById(lineSaveRequest.getDownwardTerminusId());

        Line line = Line.of(
                lineSaveRequest.getName(),
                upwardStation,
                downwardStation,
                lineSaveRequest.getDistance(),
                lineSaveRequest.getFare()
        );
        return new LineResponse(lineRepository.create(line), line.getName(), line.getFare());
    }

    private LineStationsResponse getLineStationsResponseFrom(Line line) {
        List<Section> sections = line.getSections();
        List<StationResponse> stationResponses = sections.stream()
                .map(section -> StationResponse.from(section.getUpwardStation()))
                .collect(Collectors.toList());
        stationResponses.add(getDownwardTerminusResponse(sections));
        return new LineStationsResponse(line.getId(), line.getName(), stationResponses, line.getFare());
    }

    private StationResponse getDownwardTerminusResponse(List<Section> sections) {
        return StationResponse.from(sections.get(sections.size() - 1).getDownwardStation());
    }
}
