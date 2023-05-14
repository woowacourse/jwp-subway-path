package subway.application.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.line.port.in.LineFindByIdUseCase;
import subway.application.line.port.in.LineNotFoundException;
import subway.application.line.port.in.LineResponseDto;
import subway.application.line.port.out.LineRepository;
import subway.domain.line.Line;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class LineFindByIdService implements LineFindByIdUseCase {

    private final LineRepository lineRepository;

    @Override
    public LineResponseDto findById(final Long id) {
        final Line line = lineRepository.findById(id)
            .orElseThrow(LineNotFoundException::new);
        return LineResponseDto.from(line);
    }
}
