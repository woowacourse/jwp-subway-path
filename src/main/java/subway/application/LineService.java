package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineStationResponse;
import subway.dto.StationResponse;
import subway.entity.Line;
import subway.entity.Section;
import subway.entity.Station;

import java.util.*;

@Service
public class LineService {
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineService(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Long save(LineRequest request) {
        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        return persistLine.getId();
    }

    public List<LineStationResponse> findAll() {
        List<Line> lines = lineDao.findAll();
        List<Section> sections = sectionDao.findAll();
        List<Station> stations = stationDao.findAll();

        Map<Long, List<Section>> sectionsByLine = sortSectionsByLine(sections);

        List<LineStationResponse> lineStationResponses = new ArrayList<>();
        for (Line line : lines) {
            List<StationResponse> stationResponses = linkStationsByLine(sectionsByLine.get(line.getId()), stations);
            lineStationResponses.add(LineStationResponse.from(LineResponse.of(line), stationResponses));
        }
        return lineStationResponses;
    }

    private Map<Long, List<Section>> sortSectionsByLine(final List<Section> sections) {
        Map<Long, List<Section>> sectionsByLine = new HashMap<>();
        for (Section section : sections) {
            List<Section> sectionEntities = sectionsByLine.getOrDefault(section.getLineId(), new ArrayList<>());
            sectionEntities.add(section);
            sectionsByLine.put(section.getLineId(), sectionEntities);
        }
        return sectionsByLine;
    }

    public LineStationResponse findById(Long id) {
        Optional<Line> line = lineDao.findById(id);
        if (line.isEmpty()) {
            throw new NoSuchElementException("해당하는 ID가 없습니다.");
        }
        List<Section> sections = sectionDao.findByLineId(id);
        List<Station> stations = stationDao.findAll();

        List<StationResponse> stationResponses = linkStationsByLine(sections, stations);
        return LineStationResponse.from(LineResponse.of(line.get()), stationResponses);
    }

    private List<StationResponse> linkStationsByLine(final List<Section> sections, final List<Station> stations) {
        if (sections == null) {
            return new ArrayList<>();
        }

        Map<Long, String> stationMap = new HashMap<>();
        stations.forEach(stationEntity -> stationMap.put(stationEntity.getId(), stationEntity.getName()));

        Map<Long, Long> sectionMap = new HashMap<>();
        sections.forEach(section -> sectionMap.put(section.getUpStationId(), section.getDownStationId()));

        return linkStations(stationMap, sectionMap);
    }

    private List<StationResponse> linkStations(final Map<Long, String> stationMap, final Map<Long, Long> sectionMap) {
        List<StationResponse> stationResponses = new ArrayList<>();
        Long nextStationId = sectionMap.get(null);
        while (nextStationId != null) {
            Station station = new Station(nextStationId, stationMap.get(nextStationId));
            stationResponses.add(StationResponse.of(station));
            nextStationId = sectionMap.get(nextStationId);
        }
        return stationResponses;
    }

    public void update(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(id, new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteById(Long id) {
        lineDao.deleteById(id);
    }
}
