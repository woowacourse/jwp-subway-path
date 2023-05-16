package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineStationResponse;
import subway.dto.StationResponse;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

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

    public Long save(final LineRequest request) {
        final Line line = new Line(request.getName(), request.getColor());
        return lineDao.insert(LineEntity.toEntity(line));
    }

    // TODO
    public List<LineStationResponse> findAll() {
        List<LineEntity> lines = lineDao.findAll();
        List<SectionEntity> sections = sectionDao.findAll();
        List<StationEntity> stations = stationDao.findAll();

        Map<Long, List<SectionEntity>> sectionsByLine = sortSectionsByLine(sections);

        List<LineStationResponse> lineStationResponses = new ArrayList<>();
        for (LineEntity line : lines) {
            List<StationResponse> stationResponses = linkStationsByLine(sectionsByLine.get(line.getId()), stations);
            lineStationResponses.add(LineStationResponse.from(LineResponse.of(line), stationResponses));
        }
        return lineStationResponses;
    }

    private Map<Long, List<SectionEntity>> sortSectionsByLine(final List<SectionEntity> sections) {
        Map<Long, List<SectionEntity>> sectionsByLine = new HashMap<>();
        for (SectionEntity section : sections) {
            List<SectionEntity> sectionEntities = sectionsByLine.getOrDefault(section.getLineId(), new ArrayList<>());
            sectionEntities.add(section);
            sectionsByLine.put(section.getLineId(), sectionEntities);
        }
        return sectionsByLine;
    }

    // TODO
    public LineStationResponse findById(Long id) {
        final LineEntity lineEntity = lineDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 ID가 없습니다."));
        List<SectionEntity> sections = sectionDao.findByLineId(id);
        List<StationEntity> stations = stationDao.findAll();

        List<StationResponse> stationResponses = linkStationsByLine(sections, stations);
        return LineStationResponse.from(LineResponse.of(lineEntity), stationResponses);
    }

    private List<StationResponse> linkStationsByLine(final List<SectionEntity> sections, final List<StationEntity> stations) {
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

    public void update(final Long id, final LineRequest lineUpdateRequest) {
        final Line line = new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
        lineDao.update(id, LineEntity.toEntity(line));
    }

    public void deleteById(final Long id) {
        lineDao.deleteById(id);
    }
}
