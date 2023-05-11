package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.domain.Line;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;
import subway.entity.LineEntity;
import subway.exception.NoSuchLineException;
import subway.mapper.LineMapper;
import subway.repository.LineRepository;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final LineDao lineDao;

    public LineService(final LineRepository lineRepository, final LineDao lineDao) {
        this.lineRepository = lineRepository;
        this.lineDao = lineDao;
    }

    // TODO: 이름 중복 검사
    public LineResponse saveLine(LineRequest request) {
        LineEntity persistLineEntity = lineDao.insert(new LineEntity(request.getName(), request.getColor()));
        return LineResponse.of(persistLineEntity);
    }

    public List<LineResponse> findLineResponses() {
        List<Line> lines = findLines();
        return lines.stream()
                .map(LineMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineRepository.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(NoSuchLineException::new);
        return LineMapper.toResponse(line);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(new LineEntity(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

}
