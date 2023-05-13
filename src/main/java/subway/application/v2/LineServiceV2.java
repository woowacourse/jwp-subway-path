package subway.application.v2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.request.CreateLineRequest;
import subway.application.response.LineResponse;
import subway.domain.LineDomain;
import subway.dto.StationResponse;
import subway.repository.LineRepository;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class LineServiceV2 {

    private final LineRepository lineRepository;

    public LineServiceV2(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public Long saveLine(final CreateLineRequest request) {
        return lineRepository.saveLine(request.getName(), request.getColor());
    }

    public LineResponse findByLineId(final Long lineId) {
        final LineDomain line = lineRepository.findByLineId(lineId);
        final List<StationResponse> stationResponses = collectStationResponses(line);

        return LineResponse.of(line, stationResponses);
    }

    private List<StationResponse> collectStationResponses(final LineDomain line) {
        return line.getAllStations()
                .stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    public List<LineResponse> findAll() {
        return lineRepository.findAll()
                .stream()
                .map(lineDomain -> new LineResponse(
                        lineDomain.getId(),
                        lineDomain.getName(),
                        lineDomain.getColor(),
                        collectStationResponses(lineDomain)
                )).collect(Collectors.toList());
    }
}
