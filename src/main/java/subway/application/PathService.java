package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.fee.FeePolicy;
import subway.domain.path.Path;
import subway.domain.path.PathGenerator;
import subway.domain.path.SectionEdge;
import subway.dto.PathAndFee;
import subway.exceptions.customexceptions.InvalidDataException;
import subway.exceptions.customexceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final FeePolicy feePolicy;
    private final PathGenerator pathGenerator;

    public PathService(StationDao stationDao, SectionDao sectionDao, FeePolicy feePolicy, PathGenerator pathGenerator) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.feePolicy = feePolicy;
        this.pathGenerator = pathGenerator;
    }

    public PathAndFee findShortestPathAndFee(Long sourceStationId, Long targetStationId) {
        Station sourceStation = stationDao.findById(sourceStationId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 역입니다."));
        Station targetStation = stationDao.findById(targetStationId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 역입니다."));
        List<Station> stations = stationDao.findAll();
        List<Section> sections = sectionDao.findAll();

        Path path = getPath(stations, sections);
        List<Section> shortestSectionPath = path.getShortestSectionPath(sourceStation, targetStation)
                .stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());
        int fee = feePolicy.calculateFee(shortestSectionPath);

        List<Station> shortestStationPath = path.getShortestStationPath(sourceStation, targetStation);
        if (shortestStationPath.isEmpty()) {
            throw new InvalidDataException("연결되지 않은 역입니다.");
        }
        return new PathAndFee(shortestStationPath, fee);
    }

    private Path getPath(List<Station> stations, List<Section> sections) {
        Path path = pathGenerator.generate();

        for (Station station : stations) {
            path.addStation(station);
        }
        for (Section section : sections) {
            Station sourceStation = stations.stream()
                    .filter(station -> station.getId().equals(section.getUpBoundStationId()))
                    .findAny()
                    .orElseThrow(() -> new NotFoundException("해당하는 역을 찾을 수 없습니다."));
            Station targetStation = stations.stream()
                    .filter(station -> station.getId().equals(section.getDownBoundStationId()))
                    .findAny()
                    .orElseThrow(() -> new NotFoundException("해당하는 역을 찾을 수 없습니다."));

            path.addSectionEdge(sourceStation, targetStation, new SectionEdge(section));
        }
        return path;
    }
}
