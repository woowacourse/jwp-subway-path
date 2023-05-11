package subway.dao;

import org.springframework.stereotype.Repository;
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

    public SubwayMapRepository(StationDao stationDao, SectionDao sectionDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    public SubwayMap findByLineId(final Long id) {
        final List<Station> stations = stationDao.findAll();
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(id);
        return generateMap(stations, sectionEntities);
    }

    public SubwayMap find() {
        List<Station> stations = stationDao.findAll();
        List<SectionEntity> sectionEntities = sectionDao.findAll();

        return generateMap(stations, sectionEntities);
    }

    private SubwayMap generateMap(List<Station> stations, List<SectionEntity> sectionEntities) {
        return new SubwayMap(stations.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        station -> new Sections(mapToSection(sectionEntities, station))
                )));
    }

    private List<Section> mapToSection(List<SectionEntity> sectionEntities, Station station) {
        return sectionEntities.stream()
                .filter(entity -> entity.getDepartureId() == station.getId())
                .map(entity -> new Section(entity.getDistance(),
                        station, stationDao.findById(entity.getArrivalId()),
                        lineDao.findById(entity.getLineId()))
                ).collect(Collectors.toList());
    }

    public void save(SubwayMap subwayMap) {
        // 있으면 update 없으면 save
        stationDao.deleteAll();
        sectionDao.deleteAll();
        lineDao.deleteAll();

        Map<Station, Sections> subwayMap1 = subwayMap.getSubwayMap();
        subwayMap1.keySet().forEach(stationDao::insert);

        List<Section> sections = mapToSections(subwayMap1);
        sections.forEach(sectionDao::insertSection);

        Set<Line> lines = new HashSet<>(mapToLines(sections));
        lines.forEach(lineDao::insert);
    }

    private List<Section> mapToSections(Map<Station, Sections> subwayMap1) {
        return subwayMap1.values().stream()
                .map(Sections::getSections)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<Line> mapToLines(List<Section> sections) {
        return sections.stream()
                .map(Section::getLine)
                .collect(Collectors.toList());
    }
}
