package subway.dao;

import org.springframework.stereotype.Repository;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.domain.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

        final Map<Line, Optional<Station>> lineMap = lineEntities.stream()
                .collect(Collectors.toMap(LineEntity::toLine,
                        entity -> stationDao.findById(entity.getUpEndpointId()))
                );

        return generateMap(stations, sectionEntities, new HashMap<>(lineMap));
    }

    private SubwayMap generateMap(final List<Station> stations, final List<SectionEntity> sectionEntities, final Map<Line, Optional<Station>> lineMap) {
        return new SubwayMap(stations.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        station -> new Sections(mapToSection(sectionEntities, station))
                )), lineMap);
    }

    private List<Section> mapToSection(final List<SectionEntity> sectionEntities, final Station station) {
        return sectionEntities.stream()
                .filter(entity -> entity.getDepartureId().equals(station.getId()))
                .map(entity -> new Section(entity.getDistance(),
                        station,
                        stationDao.findById(entity.getArrivalId())  // TODO stations에서 직접 byId로 꺼내와도 됨
                                .orElseThrow(() -> new IllegalArgumentException("도착역이 존재하지 않습니다.")),
                        lineDao.findById(entity.getLineId()))
                ).collect(Collectors.toList());
    }

    public void save(final SubwayMap subwayMap) {
        stationDao.deleteAll();
        sectionDao.deleteAll();
        lineDao.deleteAll();

        final Map<Station, Sections> subwayMap1 = subwayMap.getSubwayMap();

        List<Station> insertedStations = subwayMap1.keySet().stream()
                .map(stationDao::insert)
                .collect(Collectors.toList());

        var endpointMap = subwayMap.getEndpointMap();

        setEndpointIdFromInserted(insertedStations, endpointMap);

        List<LineEntity> lineEntities = endpointMap.keySet().stream()
                .map(line -> new LineEntity(line, endpointMap.get(line)
                        .map(Station::getId)
                        .orElse(null)))
                .collect(Collectors.toList());

        List<Line> insertedLines = lineEntities.stream()
                .map(lineDao::insert)
                .collect(Collectors.toList());

        final List<Section> sections = mapToSections(subwayMap1);
        setStationIdFromInserted(sections, insertedStations);
        setLineIdFromInserted(sections, insertedLines);

        sections.forEach(sectionDao::insertSection);
    }

    private void setEndpointIdFromInserted(List<Station> insertedStations, Map<Line, Optional<Station>> endpointMap) {
        for (Line line : endpointMap.keySet()) {
            for (Station station : insertedStations) {
                endpointMap.get(line).ifPresent(
                        s -> {
                            if (s.getName().equals(station.getName())) {
                                s.setId(station.getId());
                            }
                        }
                );
            }
        }
    }

    private void setLineIdFromInserted(List<Section> sections, List<Line> insertedLines) {
        for (Section section : sections) {
            for (Line line : insertedLines) {
                if (section.getLine().getName().equals(line.getName())) {
                    section.setLineId(line.getId());
                }
            }
        }
    }

    private void setStationIdFromInserted(List<Section> sections, List<Station> stations) {
        for (Section section : sections) {
            for (Station station : stations) {
                if (section.getDeparture().getName().equals(station.getName())) {
                    section.getDeparture().setId(station.getId());
                }

                if (section.getArrival().getName().equals(station.getName())) {
                    section.getArrival().setId(station.getId());
                }
            }
        }
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
