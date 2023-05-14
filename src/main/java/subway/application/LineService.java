package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Subway;
import subway.dto.AddStationRequest;
import subway.dto.DeleteStationRequest;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SaveResponse;
import subway.repository.LineRepository;

@Transactional
@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public SaveResponse saveLine(LineRequest request) {
        Line line = request.toDomain();
        Long saveId = lineRepository.save(line);
        return new SaveResponse(saveId);
    }

    public void addStation(AddStationRequest request) {
        Subway subway = new Subway(lineRepository.findAll());

        subway.addStation(
                request.getLineName(),
                request.getSourceStation(),
                request.getTargetStation(),
                request.getDistance()
        );
        Line line = subway.findLineByName(request.getLineName());
        lineRepository.save(line);
    }

    public void deleteStation(DeleteStationRequest request) {
        Subway subway = new Subway(lineRepository.findAll());
        subway.removeStation(request.getLineName(), request.getStationName());
        Line line = subway.findLineByName(request.getLineName());
        lineRepository.save(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        Subway subway = new Subway(lineRepository.findAll());
        return subway.getLines().stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long lineId) {
        Line line = lineRepository.findById(lineId);
        return LineResponse.from(line);
    }
}
