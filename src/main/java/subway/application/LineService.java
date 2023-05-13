package subway.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.LineEntity;
import subway.dao.SectionDao;
import subway.dao.SectionEntity;
import subway.dao.StationDao;
import subway.dao.StationEntity;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;

@Transactional(readOnly = true)
@Service
public class LineService {
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Long savedId = lineDao.insert(new LineEntity(request.getLineName()));

        return new LineResponse(savedId, request.getLineName(), null);
    }

    public List<LineResponse> findAllLines() {
        List<LineEntity> existsLines = lineDao.findAll();

        return existsLines.stream()
                .map(entities -> {
                    Long lineId = entities.getId();
                    return getLineResponse(entities, lineId);
                })
                .collect(Collectors.toList());
    }

    private LineResponse getLineResponse(LineEntity entities, Long lineId) {
        List<SectionEntity> findSections = sectionDao.findByLineId(lineId);

        List<Section> existSections = findSections.stream()
                .map(this::toSection)
                .collect(Collectors.toList());

        if (existSections.isEmpty()) {
            return new LineResponse(entities.getId(), entities.getName(), new ArrayList<>());
        }

        return getSortedLineResponse(entities, existSections);
    }

    private LineResponse getSortedLineResponse(LineEntity entities, List<Section> existSections) {
        Sections sections = new Sections(existSections);
        List<Station> sortedStations = sections.getSortedStations();

        List<StationResponse> stationsResponses = sortedStations.stream()
                .map(it -> {
                    Long findStationId = stationDao.findIdByName(it.getName());
                    return new StationResponse(findStationId, it.getName());
                })
                .collect(Collectors.toList());

        return new LineResponse(entities.getId(), entities.getName(), stationsResponses);
    }

    public LineResponse findLineResponseById(Long id) {
        LineEntity findEntity = lineDao.findById(id);
        return getLineResponse(findEntity, id);
    }

    private Station toStation(StationEntity stationEntity) {
        return new Station(stationEntity.getName());
    }

    private Section toSection(SectionEntity sectionEntity) {
        Station startStation = toStation(stationDao.findById(sectionEntity.getStartStationId()));
        Station endStation = toStation(stationDao.findById(sectionEntity.getEndStationId()));
        Distance distance = new Distance(sectionEntity.getDistance());

        return new Section(startStation, endStation, distance);
    }

}
