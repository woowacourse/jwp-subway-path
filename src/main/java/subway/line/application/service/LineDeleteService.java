package subway.line.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.in.delete.LineDeleteUseCase;
import subway.line.application.port.out.LineRepository;

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
