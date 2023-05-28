package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.request.CreateLineRequest;
import subway.application.response.LineResponse;
import subway.application.response.StationResponse;
import subway.dao.entity.LineExpenseEntity;
import subway.domain.Line;
import subway.repository.FareRepository;
import subway.repository.LineRepository;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;
    private final FareRepository fareRepository;

    public LineService(final LineRepository lineRepository, final FareRepository fareRepository) {
        this.lineRepository = lineRepository;
        this.fareRepository = fareRepository;
    }

    @Transactional
    public Long saveLine(final CreateLineRequest request) {
        final Long saveLineId = lineRepository.saveLine(request.getName(), request.getColor());
        fareRepository.saveLineExpense(LineExpenseEntity.freeExpenseWithLineId(saveLineId));

        return saveLineId;
    }

    public LineResponse findByLineId(final Long lineId) {
        final Line line = lineRepository.findByLineId(lineId);
        final List<StationResponse> stationResponses = collectStationResponses(line);

        return LineResponse.of(line, stationResponses);
    }

    private List<StationResponse> collectStationResponses(final Line line) {
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
                        lineDomain.getNameValue(),
                        lineDomain.getColorValue(),
                        collectStationResponses(lineDomain)
                )).collect(Collectors.toList());
    }
}
