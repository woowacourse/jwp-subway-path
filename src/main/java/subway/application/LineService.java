package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.domain.station.StationConnections;
import subway.dto.LineFindResponse;
import subway.entity.LineEntity;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionRepository sectionRepository;

    public LineService(LineDao lineDao, SectionRepository sectionRepository) {
        this.lineDao = lineDao;
        this.sectionRepository = sectionRepository;
    }

    public LineFindResponse findStationNamesByLineId(Long lineId) {
        LineEntity lineEntity = lineDao.findById(lineId);
        List<Section> findSections = sectionRepository.findSectionsByLineId(lineId);
        StationConnections stationConnections = StationConnections.fromSections(findSections);
        return new LineFindResponse(lineEntity.getLineName(), stationConnections.getSortedStationNames());
    }

    public List<LineFindResponse> findAllLineStationNames() {
        List<LineEntity> lineEntities = lineDao.findAll();
        return lineEntities.stream()
                .map(lineEntity -> findStationNamesByLineId(lineEntity.getId()))
                .collect(toList());
    }
}
