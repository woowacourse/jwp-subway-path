package subway.domain.line.service;

import org.springframework.stereotype.Service;
import subway.domain.line.domain.Line;
import subway.domain.line.repository.LineRepository;

import java.util.List;

@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Line findById(Long id) {
        return lineRepository.findById(id);
    }

    public List<Line> findAll() {
        return lineRepository.findAll();
    }
}
