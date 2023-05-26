package subway.service.line;

import org.springframework.stereotype.Service;
import subway.service.line.domain.Line;
import subway.service.line.dto.LineInsertRequest;
import subway.service.line.dto.LineResponse;
import subway.service.section.SectionCaching;
import subway.service.section.domain.Sections;
import subway.service.station.domain.Station;
import subway.service.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineRepository lineRepository;
    private final SectionCaching sectionCaching;

    public LineService(LineRepository lineRepository, SectionCaching sectionCaching) {
        this.lineRepository = lineRepository;
        this.sectionCaching = sectionCaching;
    }

    public LineResponse saveLine(LineInsertRequest request) {
        Line persistLine = lineRepository.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }

    public List<StationResponse> findStationsByLine(long lineId) {
        Line line = lineRepository.findById(lineId);

        Sections sections = sectionCaching.getSections(line);
        List<Station> stationsInOrder = sections.orderStations();

        return stationsInOrder.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

}
