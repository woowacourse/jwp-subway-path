package subway.application.line.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.line.port.in.LineResponseDto;
import subway.application.line.port.in.findall.LineFindAllUseCase;
import subway.application.line.port.out.LineRepository;

@Service
@Transactional(readOnly = true)
public class LineFindAllService implements LineFindAllUseCase {

    private final LineRepository lineRepository;

    public LineFindAllService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Override
    public List<LineResponseDto> findAllLines() {
        return lineRepository.findAll()
                .stream()
                .map(LineResponseDto::from)
                .collect(Collectors.toList());
    }
}
