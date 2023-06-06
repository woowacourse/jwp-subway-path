package subway.line.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.exception.LineNotFoundException;
import subway.line.domain.line.Line;
import subway.line.domain.line.LineRepository;
import subway.line.dto.response.LineResponseDto;

@Service
@Transactional(readOnly = true)
public class LineQueryService {

    private final LineRepository lineRepository;

    public LineQueryService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public List<LineResponseDto> findAllLines() {
        return lineRepository.findAll()
                .stream()
                .map(LineResponseDto::from)
                .collect(Collectors.toList());
    }

    public LineResponseDto findById(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
        return LineResponseDto.from(line);
    }
}
