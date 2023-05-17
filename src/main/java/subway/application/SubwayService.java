package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDAO;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.LineSections;
import subway.domain.Route;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.SubwayGraph;
import subway.dto.SubwayResponse;

@Service
public class SubwayService {
    
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDAO sectionDAO;
    
    public SubwayService(final LineDao lineDao, final StationDao stationDao, final SectionDAO sectionDAO) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDAO = sectionDAO;
    }
    
    public SubwayResponse findAllStationsInLine(final long lineId) {
        final Line line = this.lineDao.findById(lineId);
        final List<Section> sections = this.sectionDAO.findSectionsBy(lineId);
        final LineSections lineSections = LineSections.from(sections);
        
        if (lineSections.isEmpty()) {
            return SubwayResponse.of(line, List.of());
        }
        
        final long upTerminalStationId = lineSections.getUpTerminalStationId();
        final long downTerminalStationId = lineSections.getDownTerminalStationId();
        
        final SubwayGraph subwayGraph = SubwayGraph.from(sections, upTerminalStationId, downTerminalStationId);
        final Route route = subwayGraph.getRoute();
        final List<Station> orderedStations = route.getStationIds()
                .stream()
                .map(this.stationDao::findById)
                .collect(Collectors.toUnmodifiableList());
        return SubwayResponse.of(line, orderedStations);
    }
    
    public List<SubwayResponse> findAllStations() {
        final List<Line> lines = this.lineDao.findAll();
        return lines.stream()
                .map(line -> this.findAllStationsInLine(line.getId()))
                .collect(Collectors.toUnmodifiableList());
    }
}

