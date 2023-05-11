package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.domain.*;
import subway.dto.*;

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

    public List<FinalLineResponse> findLineResponses() {
        final List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(FinalLineResponse::from)
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineDao.findAll().stream()
                .map(line -> findById(line.getId()))
                .collect(Collectors.toList());
    }

    public FinalLineResponse findLineResponseById(final Long id) {
        final Line line = findById(id);
        return FinalLineResponse.from(line);
    }

    @Transactional
    public void registerStation(final Long lineId, final SectionRequest request) {
        final Line line = findById(lineId);

        final Section section = createSections(request);
        final Line addedLine = line.addSection(section);

        final Sections deleteSections = line.getSections().getDifferenceOfSet(addedLine.getSections());
        final Sections insertSections = addedLine.getSections().getDifferenceOfSet(line.getSections());

        sectionService.deleteSections(deleteSections);
        sectionService.insertSections(line.getId(), insertSections);
    }

    @Transactional
    public void unregisterStation(final Long id, final StationRequest request) {
        final Line line = findById(id);

        final Station station = stationService.findStationByName(request.getName());
        final Line deletedLine = line.removeStation(station);


        final Sections deleteSections = line.getSections().getDifferenceOfSet(deletedLine.getSections());
        final Sections insertSections = deletedLine.getSections().getDifferenceOfSet(line.getSections());

        sectionService.deleteSections(deleteSections);
        sectionService.insertSections(line.getId(), insertSections);
    }

    public Line findById(final Long id) {
        final Line emptyLine = lineDao.findById(id);
        final Sections sections = sectionService.findByLineId(emptyLine.getId());
        return new Line(emptyLine.getId(), emptyLine.getName(), sections);
    }

    private Section createSections(final SectionRequest request) {
        final Station beforeStation = stationService.findStationByName(request.getBeforeStationName());
        final Station nextStation = stationService.findStationByName(request.getNextStationName());
        return new Section(beforeStation, nextStation, new Distance(request.getDistance()));
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        lineDao.updateName(id, new LineName(lineUpdateRequest.getName()));
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }
}
