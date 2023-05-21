package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;
import subway.mapper.LineMapper;
import subway.repository.LineRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id);
        return LineMapper.toResponse(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLine() {
        return lineRepository.findAll().stream()
                .map(LineMapper::toResponse)
                .collect(Collectors.toList());
    }

    public LineResponse saveLine(LineRequest request) {
        Line line = LineMapper.toLine(request);
        Line saved = lineRepository.insert(line);
        return LineMapper.toResponse(saved);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line line = LineMapper.toLine(lineUpdateRequest);
        lineRepository.update(id, line);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
