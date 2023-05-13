package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.domain.Line;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;
import subway.exceptions.customexceptions.InvalidDataException;
import subway.exceptions.customexceptions.NotFoundException;

import java.util.List;

@Transactional
@Service
public class LineService {
    private final LineDao lineDao;

    public LineService(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public LineResponse saveLine(LineRequest request) {
        if (lineDao.findByName(request.getName()).isPresent()) {
            throw new InvalidDataException("이미 존재하는 라인입니다.");
        }
        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public List<Line> findLines() {
        return lineDao.findAll();
    }

    @Transactional(readOnly = true)
    public Line findLineById(Long id) {
        return lineDao.findById(id)
                .orElseThrow(() -> new NotFoundException("해당하는 라인이 존재하지 않습니다."));
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        findLineById(id);
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        findLineById(id);
        lineDao.deleteById(id);
    }
}
