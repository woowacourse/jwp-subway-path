package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.entity.LineEntity;
import subway.dao.vo.SectionStationMapper;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;
import subway.dto.response.LineSectionResponse;
import subway.exception.DuplicatedException;
import subway.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

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

    public LineSectionResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineSectionResponse.of(persistLine);
    }

    private Line findLineById(Long lineId) {
        LineEntity lineEntity = lineDao.findById(lineId)
                                       .orElseThrow(() -> new NotFoundException("해당 노선이 존재하지 않습니다"));
        List<SectionStationMapper> sectionStationMappers = sectionDao.findSectionsByLineId(lineId);
        List<Section> sections = sectionStationMappers.stream()
                                                      .map(Section::from)
                                                      .collect(Collectors.toList());
        return new Line(lineId, lineEntity.getName(), lineEntity.getColor(), new Sections(sections));
    }

    public List<LineSectionResponse> findLineResponses() {
        List<Line> persistLine = findLines();
        return persistLine.stream()
                          .map(LineSectionResponse::of)
                          .collect(Collectors.toList());
    }

    private List<Line> findLines() {
        List<LineEntity> lineEntities = lineDao.findAll();
        return lineEntities.stream()
                           .map(lineEntity -> findLineById(lineEntity.getId()))
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
        if (lineDao.isExistId(lineId)) {
            return;
        }
        throw new NotFoundException("해당 노선이 존재하지 않습니다.");
    }

    private void validateDuplicateLine(LineRequest request) {
        if (lineDao.isExistName(request.getName())) {
            throw new DuplicatedException("이미 존재하는 노선 이름입니다.");
        }
        if (lineDao.isExistColor(request.getColor())) {
            throw new DuplicatedException("이미 존재하는 노선 색입니다.");
        }
    }

}
