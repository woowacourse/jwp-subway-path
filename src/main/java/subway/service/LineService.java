package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.entity.LineEntity;
import subway.repository.LineRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;

    @Transactional
    public LineResponse saveLine(final LineRequest request) {
        final Line line = new Line(request.getName(), request.getColor());
        final Line savedLine = lineRepository.save(line);
        return new LineResponse(savedLine.getId(), savedLine.getName(), savedLine.getColor());
    }

    public List<LineResponse> findLineResponses() {
        final List<LineEntity> persistLineEntities = findLines();
        return persistLineEntities.stream().map(LineResponse::of).collect(Collectors.toList());
    }

    public List<LineEntity> findLines() {
        final List<Line> lines = lineRepository.findAll();
        return null;
    }

    public LineResponse findLineResponseById(final Long id) {
        final LineEntity persistLineEntity = findLineById(id);
        return LineResponse.of(persistLineEntity);
    }

    public LineEntity findLineById(final Long id) {
        return null;
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
    }

    public void deleteLineById(final Long id) {
    }

}
