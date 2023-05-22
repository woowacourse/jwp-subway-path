package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.*;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.repository.LineRepository;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public Long saveLine(final LineRequest request) {
        Lines lines = new Lines(lineRepository.findAll());
        Line line = new Line(new LineName(request.getName()), new LineColor(request.getColor()), Sections.create());
        lines.validateNotDuplicatedLine(line);
        Line savedLine = lineRepository.save(line);
        return savedLine.getId();
    }

    public LineResponse findLine(final Long lineId) {
        Line line = lineRepository.findById(lineId);
        return LineResponse.from(line);
    }

    @Transactional
    public void editLine(final Long lineId, final LineRequest request) {
        Lines lines = new Lines(lineRepository.findAll());
        Line line = lineRepository.findById(lineId);
        Line updateLine = new Line(
                lineId,
                new LineName(request.getName()),
                new LineColor(request.getColor()),
                new Sections(line.getSections())
        );
        lines.deleteById(lineId).validateNotDuplicatedLine(updateLine);
        lineRepository.update(updateLine);
    }

    @Transactional
    public void deleteLine(final Long lineId) {
        Line line = lineRepository.findById(lineId);
        lineRepository.delete(line);
    }
}
