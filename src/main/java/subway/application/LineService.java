package subway.application;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.dto.LineResponse;
import subway.dto.LineSaveRequest;
import subway.dto.LineUpdateRequest;
import subway.exception.LineAlreadyExistsException;
import subway.exception.LineNotFoundException;
import subway.repository.LineRepository;

@Transactional
@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Long save(final LineSaveRequest request) {
        final Optional<Long> lineId = lineRepository.findIdByName(request.getName());
        if (lineId.isPresent()) {
            throw new LineAlreadyExistsException();
        }
        lineRepository.save(new Line(request.getName(), request.getColor(), Collections.emptyList()));
        return lineRepository.findIdByName(request.getName())
                .orElseThrow(LineNotFoundException::new);
    }

    public void delete(final Long id) {
        lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
        lineRepository.deleteById(id);
    }

    public void update(final Long id, final LineUpdateRequest request) {
        lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
        lineRepository.updateNameAndColorById(id, request.getName(), request.getColor());
    }

    @Transactional(readOnly = true)
    public LineResponse findById(final Long id) {
        final Line line = lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
        return LineResponse.from(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        return lineRepository.findAll().stream()
                .map(LineResponse::from)
                .collect(toList());
    }
}
