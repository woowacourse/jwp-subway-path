package subway.application;

import static java.util.Comparator.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.ui.dto.GetLineResponse;
import subway.ui.dto.LineRequest;
import subway.ui.dto.PostLineResponse;

@Service
@Transactional
public class LineService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PostLineResponse saveLine(LineRequest request) {
        Optional<Station> upStation = stationDao.findById(request.getUpStationId());
        Optional<Station> downStation = stationDao.findById(request.getDownStationId());
        validateStationsPresence(upStation, downStation);

        Line line = lineDao.insert(new Line(request.getName(), request.getColor()));
        sectionDao.insert(new Section(upStation.get(), downStation.get(), line, request.getDistance()));
        return PostLineResponse.of(line);
    }

    private void validateStationsPresence(Optional<Station> upStation, Optional<Station> downStation) {
        if (upStation.isEmpty() || downStation.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 역이 있습니다.");
        }
    }

    public List<GetLineResponse> findAllLines() {
        List<Line> lines = lineDao.findAll();
        return lines.stream()
            .sorted(comparing(Line::getId))
            .map(this::findLineResponse)
            .collect(Collectors.toList());
    }

    public GetLineResponse findLineById(Long id) {
        Line line = lineDao.findById(id);
        return findLineResponse(line);
    }

    private GetLineResponse findLineResponse(Line line) {
        Sections sections = new Sections(sectionDao.findAllByLineId(line.getId()));
        List<Station> stations = sections.getStationsInOrder();
        return GetLineResponse.from(line, stations);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        sectionDao.deleteByLineId(id);
        lineDao.deleteById(id);
    }
}
