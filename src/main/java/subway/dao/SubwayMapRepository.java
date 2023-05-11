package subway.dao;

import org.springframework.stereotype.Repository;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.domain.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class SubwayMapRepository {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public SubwayMapRepository(final StationDao stationDao, final SectionDao sectionDao, final LineDao lineDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    public SubwayMap find() {
        final List<Station> stations = stationDao.findAll();
        final List<SectionEntity> sectionEntities = sectionDao.findAll();
        final List<LineEntity> lineEntities = lineDao.findAll();
        final Map<Line, Station> lineMap = lineEntities.stream()
                .collect(Collectors.toMap(entity -> entity.toLine(),
                        entity -> stations.stream()
                                .filter(station -> station.getId().equals(entity.getUpEndpointId()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 종점역입니다."))
                ));
        return generateMap(stations, sectionEntities, lineMap);
    }

    private SubwayMap generateMap(final List<Station> stations, final List<SectionEntity> sectionEntities, final Map<Line, Station> lineMap) {
        return new SubwayMap(stations.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        station -> new Sections(mapToSection(sectionEntities, station))
                )), lineMap);
    }

    private List<Section> mapToSection(final List<SectionEntity> sectionEntities, final Station station) {
        return sectionEntities.stream()
                .filter(entity -> entity.getDepartureId() == station.getId())
                .map(entity -> new Section(entity.getDistance(),
                        station, stationDao.findById(entity.getArrivalId()),
                        lineDao.findById(entity.getLineId()))
                ).collect(Collectors.toList());
    }

    public void save(final SubwayMap subwayMap) {
        // 있으면 update 없으면 save
        stationDao.deleteAll();
        sectionDao.deleteAll();
        lineDao.deleteAll();

        final Map<Station, Sections> subwayMap1 = subwayMap.getSubwayMap();
        subwayMap1.keySet().forEach(stationDao::insert);

        final List<Section> sections = mapToSections(subwayMap1);
        sections.forEach(sectionDao::insertSection);

        final Set<Line> lines = new HashSet<>(mapToLines(sections));
        final List<LineEntity> lineEntities = lines.stream()
                .map(line -> new LineEntity(line.getId(), line.getName(), line.getColor(), subwayMap.getEndpointMap().get(line).getId()))
                .collect(Collectors.toList());
        lineEntities.forEach(lineDao::insert);
    }

    private List<Section> mapToSections(final Map<Station, Sections> subwayMap1) {
        return subwayMap1.values().stream()
                .map(Sections::getSections)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<Line> mapToLines(final List<Section> sections) {
        return sections.stream()
                .map(Section::getLine)
                .collect(Collectors.toList());
    }
}
