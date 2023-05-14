package subway.application.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subway.application.line.port.in.LineDeleteUseCase;
import subway.application.line.port.out.LineRepository;

@RequiredArgsConstructor
@Service
public class LineDeleteService implements LineDeleteUseCase {

    private final LineRepository lineRepository;

    @Override
    public void deleteLineById(final Long id) {
        lineRepository.deleteById(id);
    }
}
