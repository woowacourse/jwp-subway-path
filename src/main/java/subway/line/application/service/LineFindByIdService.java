package subway.line.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.in.LineNotFoundException;
import subway.line.application.port.in.LineResponseDto;
import subway.line.application.port.in.findById.LineFindByIdUseCase;
import subway.line.application.port.out.LineRepository;
import subway.line.domain.Line;

@Service
@Transactional(readOnly = true)
public class LineFindByIdService implements LineFindByIdUseCase {

    private final LineRepository lineRepository;

    public LineFindByIdService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Override
    public LineResponseDto findById(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
        return LineResponseDto.from(line);
    }
}
