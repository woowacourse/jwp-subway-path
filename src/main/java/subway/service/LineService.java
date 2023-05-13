package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;
import subway.mapper.LineMapper;
import subway.repository.LineRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line line = LineMapper.toLine(request);
        Line saved = lineRepository.insert(line);
        return LineMapper.toResponse(saved);
    }

    public List<LineResponse> findLineResponses() {
        return lineRepository.findAll().stream()
                .map(LineMapper::toResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long id) {
        Line line = lineRepository.findById(id);
        return LineMapper.toResponse(line);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line line = LineMapper.toLine(lineUpdateRequest);
        lineRepository.update(id, line);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
