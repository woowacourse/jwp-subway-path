package subway.application.line;

import org.springframework.stereotype.Service;
import subway.domain.repository.LineRepository;

@Service
public class DeleteLineService {

    private final LineRepository lineRepository;

    public DeleteLineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public void deleteLine(final Long lineIdRequest) {
        lineRepository.deleteById(lineIdRequest);
    }
}
