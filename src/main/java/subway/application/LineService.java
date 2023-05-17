package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionRepository;
import subway.domain.Line;
import subway.domain.Sections;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.exception.custom.LineNotExistException;

@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionRepository sectionRepository;

    public LineService(final LineDao lineDao, final SectionRepository sectionRepository) {
        this.lineDao = lineDao;
        this.sectionRepository = sectionRepository;
    }

    public LineResponse saveLine(final LineRequest request) {
        final Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLineResponses() {
        return findLines().stream()
            .flatMap(line -> Stream.of(findLineResponseById(line.getId())))
            .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(final Long id) {
        final Line persistLine = findLineById(id);
        final Sections sections = new Sections(sectionRepository.findAllByLineId(id));
        final List<StationResponse> stationResponses = sections.getSortedStations()
            .stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());

        return LineResponse.withStationReponses(persistLine, stationResponses);
    }

    public Line findLineById(final Long id) {
        return lineDao.findById(id)
            .orElseThrow(() -> new LineNotExistException("노선이 존재하지 않습니다."));
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }
}
