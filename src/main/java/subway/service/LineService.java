package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.line.LineCreateRequest;
import subway.dto.line.LinesResponse;
import subway.entity.LineEntity;
import subway.repository.LineRepository;

@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public Long saveLine(final LineCreateRequest request) {
        return lineRepository.insertLine(new LineEntity(null, request.getLineNumber(), request.getName(), request.getColor()));
    }

    @Transactional(readOnly = true)
    public LinesResponse findAll() {
        return LinesResponse.from(lineRepository.findAll());
    }

    @Transactional
    public void deleteLineById(final Long id) {
        lineRepository.deleteLineById(id);
    }
}
