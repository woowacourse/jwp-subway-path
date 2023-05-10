package subway.application;


import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.LineQueryResponse;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.exception.NotFoundLineException;

@Service
@Transactional(readOnly = true)
public class LineQueryService {

    private final LineRepository lineRepository;

    public LineQueryService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineQueryResponse findById(final Long id) {
        final Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundLineException(id));
        return LineQueryResponse.from(line);
    }

    public List<LineQueryResponse> findAll() {
        return lineRepository.findAll().stream()
                .map(LineQueryResponse::from)
                .collect(Collectors.toList());
    }
}
