package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.dto.LineAndStationsResponse;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationAddRequest;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository, final SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public LineResponse saveLine(final LineRequest request) {
        final Line line = new Line(request.getName(), request.getColor());
        validateDuplication(line);
        return LineResponse.of(lineRepository.save(line));
    }

    private void validateDuplication(final Line line) {
        if (lineRepository.contains(line)) {
            throw new IllegalArgumentException("이미 존재하는 호선입니다.");
        }
    }

    public List<LineAndStationsResponse> findLines() {
        final List<Line> lines = lineRepository.findLines();
        return lines.stream()
                .map(LineAndStationsResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }

    public LineAndStationsResponse findLineById(final Long id) {
        return LineAndStationsResponse.of(lineRepository.findLineById(id));
    }

    public LineAndStationsResponse addStationToLine(final Long lineId, final StationAddRequest stationAddRequest) {
        final Line line = lineRepository.findLineById(lineId);
        final Station from = stationRepository.findStationById(stationAddRequest.getFromId());
        final Station to = stationRepository.findStationById(stationAddRequest.getToId());

        final Line insertedLine = line.insert(from, to, stationAddRequest.getDistance());

        sectionRepository.saveUpdatedSections(insertedLine.getSections(), lineId);
        return LineAndStationsResponse.of(insertedLine);
    }

    public void deleteStationFromLine(final Long lineId, final Long stationId) {
        final Line line = lineRepository.findLineById(lineId);
        final Station station = stationRepository.findStationById(stationId);

        final Line deletedLine = line.delete(station);

        sectionRepository.saveUpdatedSections(deletedLine.getSections(), lineId);
    }
}
