package subway.application;

import org.springframework.stereotype.Service;
import subway.application.dto.CreationLineDto;
import subway.dao.LineDao;
import subway.domain.Line;
import subway.dto.LineRequest;
import subway.dto.ReadLineResponse;
import subway.ui.dto.request.CreationLineRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineDao lineDao;

    public LineService(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public CreationLineDto saveLine(CreationLineRequest request) {
        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        return CreationLineDto.from(persistLine);
    }

    public List<ReadLineResponse> findLineResponses() {
        List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(ReadLineResponse::of)
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public ReadLineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return ReadLineResponse.of(persistLine);
    }

    public Line findLineById(Long id) {
        return lineDao.findById(id);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

}
