package subway.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.PathInformation;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.farecalculator.FareCalculator;
import subway.domain.pathfinder.PathFinder;
import subway.dto.FareResponse;
import subway.dto.PathResponse;
import subway.dto.SectionMapper;
import subway.dto.SectionResponse;
import subway.exception.DomainException;
import subway.exception.ExceptionType;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final PathFinder pathFinder;
    private final FareCalculator fareCalculator;
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(
            PathFinder pathFinder,
            FareCalculator fareCalculator,
            LineDao lineDao,
            StationDao stationDao,
            SectionDao sectionDao
    ) {
        this.pathFinder = pathFinder;
        this.fareCalculator = fareCalculator;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse computePath(Long sourceStationId, Long targetStationId, Integer age) {
        validate(sourceStationId, targetStationId);
        final List<Station> stations = stationDao.findAll();
        final List<Line> lines = lineDao.findAll();
        final List<Section> sections = sectionDao.findAll();

        pathFinder.addSections(sections);
        final PathInformation pathInformation = pathFinder.computeShortestPath(sourceStationId, targetStationId);
        final Integer distance = pathInformation.getDistance();
        final SectionMapper sectionMapper = SectionMapper.from(stations, lines);
        final List<SectionResponse> sectionResponses = sectionMapper.convertToSectionResponse(pathInformation.getPath());
        final FareResponse calculate = fareCalculator.calculate(sectionResponses, distance, age);

        return new PathResponse(distance, calculate, sectionResponses);
    }

    private void validate(Long sourceStationId, Long targetStationId) {
        if (sourceStationId.equals(targetStationId)) {
            throw new DomainException(ExceptionType.SOURCE_IS_SAME_WITH_TARGET);
        }
    }
}
