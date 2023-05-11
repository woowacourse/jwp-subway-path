package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.line.LineRequest;
import subway.entity.LineEntity;
import subway.repository.LineRepository;

import java.util.List;

@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public Long saveLine(final LineRequest request) {
        return lineRepository.insertLine(new LineEntity(null, request.getLineNumber(), request.getName(), request.getColor()));
    }

    @Transactional(readOnly = true)
    public List<LineEntity> findAll() {
        return lineRepository.findAll();
    }

    @Transactional
    public void deleteLineById(final Long id) {
        lineRepository.deleteLineById(id);
    }
}
