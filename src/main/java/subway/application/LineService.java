package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.domain.*;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final StationService stationService;
    private final SectionService sectionService;
    private final LineDao lineDao;

    public LineService(final StationService stationService, final SectionService sectionService, final LineDao lineDao) {
        this.stationService = stationService;
        this.sectionService = sectionService;
        this.lineDao = lineDao;
    }

    public LineResponse saveLine(final LineRequest request) {
        final Line persistLine = lineDao.insert(new LineName(request.getName()));
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLineResponses() {
        final List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(final Long id) {
        final Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public void registerStation(final SectionRequest request) {
        final Line line = lineDao.findLineByName(request.getLineName())
                .orElseThrow(() -> new IllegalArgumentException("노선을 찾을 수 없습니다."));
        final Station beforeStation = stationService.findStationByName(request.getBeforeStationName());
        final Station nextStation = stationService.findStationByName(request.getNextStationName());
        final Section section = new Section(beforeStation, nextStation, new Distance(request.getDistance()));
        final Line addedLine = line.addSection(section);

        final Sections deleteSections = line.getSections().getDifferenceOfSet(addedLine.getSections());
        final Sections insertSections = addedLine.getSections().getDifferenceOfSet(line.getSections());

        sectionService.delteSections(deleteSections);
        sectionService.insertSections(line.getId(), insertSections);
    }

    public Line findLineById(final Long id) {
        return lineDao.findById(id);
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        lineDao.updateName(id, new LineName(lineUpdateRequest.getName()));
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }

}
