package subway.application;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.Sections;
import subway.dto.LineRequest;
import subway.repository.LineRepository;

@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Line createLine(LineRequest request) {
        return lineRepository.saveNewLine(new Line(null, request.getName(), request.getColor(), new Sections(
                new ArrayList<>())));
    }

    public Line findLineById(Long lineId) {
        return lineRepository.findLineById(lineId);
    }

    public List<Line> findLines() {
        return lineRepository.findAll();
    }

    public void updateLineInfo(Long lineId, LineRequest request) {
        Line line = findLineById(lineId);
        Line lineToUpdate = new Line(lineId, request.getName(), request.getColor(), new Sections(line.getSections()));
        lineRepository.updateLineInfo(lineToUpdate);
    }

    public void deleteLineById(Long lineId) {
        Line line = findLineById(lineId);
        lineRepository.delete(line.getId());
    }

}
