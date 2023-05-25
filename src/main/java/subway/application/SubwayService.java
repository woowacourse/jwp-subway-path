package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.*;
import subway.dto.PathRequest;
import subway.dto.PathResponse;
import subway.dto.SectionResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubwayService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public SubwayService(final StationDao stationDao, final SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse findShortestPath(final PathRequest request) {
        List<Station> allStation = stationDao.findAll();
        List<Section> allSection = sectionDao.findAll()
                .stream()
                .map(sectionEntity -> new Section(
                        sectionEntity.getLineId(),
                        stationDao.findById(sectionEntity.getUpperStation()),
                        stationDao.findById(sectionEntity.getLowerStation()),
                        new Distance(sectionEntity.getDistance())))
                .collect(Collectors.toList());

        SubwayMap subwayMap = new SubwayMap(allStation, allSection);
        Station from = stationDao.findById(request.getFrom());
        Station to = stationDao.findById(request.getTo());
        Sections sections = subwayMap.getShortestPath(from, to);

        List<SectionResponse> sectionResponses = sections.getOrderedSections()
                .stream()
                .map(SectionResponse::from)
                .collect(Collectors.toList());
        int distance = sections.getDistanceBetween(from, to).getValue();
        int fare = FareCalculator.calculate(distance);

        return new PathResponse(sectionResponses, distance, fare);
    }
}
