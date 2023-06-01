package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.subway.*;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineService(LineDao lineDao, SectionDao sectionDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Long saveLine(LineRequest request) {
        final Line line = new Line(request.getName(), request.getColor());
        final Long lineId = lineDao.insert(line);

        final Section section = new Section(new Distance(request.getDistance()), findStationById(request.getUpStationId()), findStationById(request.getDownStationId()), lineId);
        sectionDao.insert(section);

        return lineId;
    }

    public List<LineResponse> findLineResponses() {
        List<Line> persistLines = lineDao.findAll();

        return persistLines.stream()
                .map(line -> findLineResponseById(line.getId()))
                .collect(Collectors.toList());
    }

    private List<Section> getSections(Long lineId) {
        return sectionDao.findAllByLineId(lineId).stream()
                .map(Section::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long id) {
        final List<Section> sections = getSections(id);
        Sections sortedSections = Sections.from(sections);

        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine, sortedSections.getSortedStations());
    }

    public Line findLineById(Long id) {
        return lineDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 노선을 찾을 수 없습니다."));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

    private Station findStationById(Long id) {
        return new Station(id, stationDao.findById(id).getName());
    }

}
