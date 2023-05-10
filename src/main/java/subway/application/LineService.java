package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.section.Section;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineStationResponse;
import subway.dto.StationResponse;
import subway.entity.LineEntity;
import subway.entity.StationEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineService(LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public LineResponse saveLine(LineRequest request) {
        LineEntity persistLineEntity = lineDao.insert(new LineEntity(request.getName(), request.getColor()));
        return LineResponse.of(persistLineEntity);
    }

    public List<LineResponse> findLineResponses() {
        List<LineEntity> persistLineEntities = findLines();
        return persistLineEntities.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public List<LineEntity> findLines() {
        return lineDao.findAll();
    }

//    public LineResponse findLineResponseById(Long id) {
//        Line persistLine = findLineById(id);
//        return LineResponse.of(persistLine);
//    }

    public LineStationResponse findLineById(Long id) {
        LineEntity lineEntity = lineDao.findById(id);
        List<Section> sections = sectionDao.findByLineId(id);
        Map<Long, StationEntity> mapSections = new HashMap<>();
        sections.forEach(section -> mapSections.put(section.getUpStation().getId(), section.getDownStation()));

        mapSections.forEach((stationId, station) -> System.out.println(stationId));
        mapSections.forEach((stationId, station) -> {
            System.out.println(station.getId());
            System.out.println(station.getName());
        });
        List<StationEntity> result = new ArrayList<>();

        StationEntity nextStationEntity = mapSections.get(null);
        System.out.println(nextStationEntity.getId());
        while(nextStationEntity.getId() != null) {
            result.add(nextStationEntity);
            nextStationEntity = mapSections.get(nextStationEntity.getId());
        }
        List<StationResponse> stationResponses = result.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return LineStationResponse.from(LineResponse.of(lineEntity), stationResponses);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(new LineEntity(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

}
