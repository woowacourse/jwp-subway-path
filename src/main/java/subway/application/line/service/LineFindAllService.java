package subway.application.line.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.line.port.in.LineFindAllUseCase;
import subway.application.line.port.in.LineResponseDto;
import subway.application.line.port.out.LineRepository;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class LineFindAllService implements LineFindAllUseCase {

    private final LineRepository lineRepository;

    @Override
    public List<LineResponseDto> findAllLines() {
        return lineRepository.findAll()
            .stream()
            .map(LineResponseDto::from)
            .collect(Collectors.toList());
    }
}
