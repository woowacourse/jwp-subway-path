package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;

@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionRepository sectionRepository;

    public LineService(LineDao lineDao, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineDao = lineDao;
        this.sectionRepository = sectionRepository;
    }

    public Long saveLine(LineRequest request) {
        final Line line = new Line(request.getName(), request.getColor());
        final Long lineId = lineDao.insert(line);

        final Section section = new Section(
                request.getDistance(),
                new Station(request.getUpStationId()),
                new Station(request.getDownStationId()),
                lineId
        );
        sectionRepository.insert(section);

        return lineId;
    }

    public List<LineResponse> findLineResponses() {
        List<Line> persistLines = findLines();

        return persistLines.stream()
                .map(line -> findLineResponseById(line.getId()))
                .collect(toList());
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        final Sections sections = sectionRepository.findAllByLineId(id);

        final List<Station> stations = sections.findAllStationsByOrder();

        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine, stations);
    }

    public Line findLineById(Long id) {
        return lineDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 노선을 찾을 수 없습니다."));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

}
