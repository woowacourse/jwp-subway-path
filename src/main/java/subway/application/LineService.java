package subway.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.LineEntity;
import subway.dao.SectionDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.exception.LineDuplicatedException;

@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;


    public LineService(LineDao lineDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public LineResponse saveLine(LineRequest request) {
        validateDuplicateLine(request);
        LineEntity lineEntity = new LineEntity(request.getName(), request.getColor());
        LineEntity savedLine = lineDao.insert(lineEntity);
        Line line = new Line(savedLine.getId(), savedLine.getName(), savedLine.getColor(), Sections.EMPTY_SECTION);
        return LineResponse.of(line);
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    private Line findLineById(Long lineId) {
        LineEntity lineEntity = lineDao.findById(lineId).orElseThrow(() -> new NotFoundException("해당 노선이 존재하지 않습니다"));
        List<Section> sections = sectionDao.findSectionsByLineId(lineId);
        return new Line(lineId, lineEntity.getName(), lineEntity.getColor(), new Sections(sections));
    }

    public List<LineResponse> findLineResponses() {
        List<Line> persistLine = findLines();
        return persistLine.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        List<LineEntity> lineEntities = lineDao.findAll();
        return lineEntities.stream()
                .map(lineEntity ->
                        new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(),
                                new Sections(sectionDao.findSectionsByLineId(lineEntity.getId()))
                        )
                )
                .collect(Collectors.toList());
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        checkIfExistLine(id);
        LineEntity lineEntity = new LineEntity(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor());
        lineDao.update(lineEntity);
    }

    public void deleteLineById(Long id) {
        checkIfExistLine(id);
        lineDao.deleteById(id);
    }

    private void checkIfExistLine(Long lineId) {
        lineDao.findById(lineId).orElseThrow(() -> new NotFoundException("해당 노선이 존재하지 않습니다."));
    }

    private void validateDuplicateLine(LineRequest request) {
        Optional<LineEntity> optionalLineByName = lineDao.findByName(request.getName());
        optionalLineByName.ifPresent(findUser -> {
            throw new LineDuplicatedException("이미 존재하는 노선 이름입니다.");
        });
        Optional<LineEntity> optionalLineByColor = lineDao.findByColor(request.getColor());
        optionalLineByColor.ifPresent(findUser -> {
            throw new LineDuplicatedException("이미 존재하는 노선 색입니다.");
        });
    }

}
