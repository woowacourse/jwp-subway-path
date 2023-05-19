package subway.application.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.line.port.in.delete.LineDeleteUseCase;
import subway.application.line.port.out.LineRepository;

@Service
@Transactional
public class LineDeleteService implements LineDeleteUseCase {

    private final LineRepository lineRepository;

    public LineDeleteService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Override
    public void deleteLineById(final Long id) {
        lineRepository.deleteById(id);
    }
}
