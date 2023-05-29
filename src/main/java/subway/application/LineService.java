package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Sections;
import subway.domain.Subway;
import subway.dto.LineRequest;
import subway.repository.LineRepository;

@Transactional
@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Line createLine(LineRequest request) {
        Subway subway = new Subway(lineRepository.findAll());
        Line newLine = new Line(null, request.getName(), request.getColor(), request.getCharge(), new Sections());
        subway.addLine(newLine);
        return lineRepository.saveLine(newLine);
    }

    @Transactional(readOnly = true)
    public Line findLineById(Long lineId) {
        return lineRepository.findLineById(lineId);
    }

    @Transactional(readOnly = true)
    public List<Line> findLines() {
        return lineRepository.findAll();
    }

    public void updateLineInfo(Long lineId, LineRequest request) {
        Subway subway = new Subway(lineRepository.findAll());
        subway.updateLineName(lineId, request.getName());
        subway.updateLineColor(lineId, request.getColor());
        subway.updateLineCharge(lineId, request.getCharge());
        lineRepository.updateLineInfo(subway.findLineById(lineId));
    }

    public void removeLine(Long lineId) {
        Subway subway = new Subway(lineRepository.findAll());
        Line linToRemove = subway.findLineById(lineId);
        lineRepository.delete(linToRemove);
    }

}
