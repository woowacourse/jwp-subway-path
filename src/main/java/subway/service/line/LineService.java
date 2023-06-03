package subway.service.line;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.dto.line.LineCreateRequest;
import subway.dto.line.LineResponse;
import subway.persistence.repository.line.JdbcLineRepository;

@Transactional
@Service
public class LineService {

    private final JdbcLineRepository jdbcLineRepository;

    public LineService(final JdbcLineRepository jdbcLineRepository) {
        this.jdbcLineRepository = jdbcLineRepository;
    }

    public Long save(final LineCreateRequest request) {
        final Line line = new Line(request.getLineNumber(), request.getName(), request.getColor(), request.getAdditionalFare());
        return jdbcLineRepository.save(line);
    }

    public void removeById(final Long id) {
        jdbcLineRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        return jdbcLineRepository.findAll().stream()
                .map(line -> new LineResponse(
                        line.getId(),
                        line.getLineNumber(),
                        line.getName(),
                        line.getColor(),
                        line.getAdditionalFare()))
                .collect(Collectors.toList());
    }
}
