package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.line.Color;
import subway.domain.line.Line;
import subway.domain.line.Name;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.SectionStationEntity;
import subway.entity.StationEntity;
import subway.exception.common.NotFoundLineException;
import subway.exception.common.NotFoundStationException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LineRepository {

    private static final int UPBOUND_STATION_INDEX = 0;
    private static final int DOWNBOUND_STATION_INDEX = 1;

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineRepository(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Line save(Line line) {
        LineEntity insertedLineEntity = lineDao.insert(new LineEntity(line.getName(), line.getColor()));
        return new Line(
                insertedLineEntity.getId(),
                new Name(insertedLineEntity.getName()),
                new Color(insertedLineEntity.getColor()),
                new Sections(List.of())
        );
    }

    public Line findByName(String name) {
        LineEntity lineEntity = lineDao.findByName(name)
                .orElseThrow(NotFoundLineException::new);

        List<SectionStationEntity> sectionStationEntities = sectionDao.findByLineId(lineEntity.getId());
        if (sectionStationEntities.isEmpty()) {
            return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
        }

        List<Section> sections = getSections(sectionStationEntities);
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), sections);
    }

    public Line findByStationId(Long id) {
        LineEntity lineEntity = lineDao.findByStationId(id).orElseThrow(NotFoundLineException::new);
        List<SectionStationEntity> sectionStationEntities = sectionDao.findByLineId(lineEntity.getId());

        List<Section> sections = getSections(sectionStationEntities);
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), sections);
    }

    public Line findById(Long id) {
        LineEntity lineEntity = lineDao.findById(id)
                .orElseThrow(NotFoundLineException::new);
        List<SectionStationEntity> sectionStationEntities = sectionDao.findByLineId(lineEntity.getId());

        List<Section> sections = getSections(sectionStationEntities);
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), sections);
    }

    public List<Line> readAll() {
        List<LineEntity> lineEntities = lineDao.findAll();
        List<SectionStationEntity> sectionStationEntities = sectionDao.findAll();
        List<Line> lines = new ArrayList<>();

        for (LineEntity lineEntity : lineEntities) {
            List<SectionStationEntity> lineSections = sectionStationEntities.stream().
                    filter(sectionStationEntity -> sectionStationEntity.getLineId() == lineEntity.getId())
                    .collect(Collectors.toList());
            Line line = new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), getSections(lineSections));
            lines.add(line);
        }
        return lines;
    }

    private static List<Section> getSections(List<SectionStationEntity> sectionStationEntities) {
        List<Section> sections = new ArrayList<>();
        for (SectionStationEntity sectionStationEntity : sectionStationEntities) {
            sections.add(new Section(
                    sectionStationEntity.getId(),
                    new Station(sectionStationEntity.getLeftStationId(), sectionStationEntity.getLeftStationName()),
                    new Station(sectionStationEntity.getRightStationId(), sectionStationEntity.getRightStationName()),
                    sectionStationEntity.getDistance())
            );
        }
        return sections;
    }

    public Station findByNameAndLineId(String baseStation, Long lineId) {
        StationEntity stationEntity = stationDao.findByNameAndLineId(baseStation, lineId).orElseThrow(NotFoundStationException::new);
        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    public List<Station> saveInitStations(final Section section, final Long lineId) {
        List<StationEntity> stationEntities = List.of(
                new StationEntity(section.getLeftStation().getName(), lineId),
                new StationEntity(section.getRightStation().getName(), lineId));
        List<StationEntity> insertedStationEntities = stationDao.insertInit(stationEntities);

        sectionDao.insert(new SectionEntity(insertedStationEntities.get(UPBOUND_STATION_INDEX).getId(),
                insertedStationEntities.get(DOWNBOUND_STATION_INDEX).getId(),
                lineId,
                section.getDistance()));

        return List.of(
                new Station(insertedStationEntities.get(UPBOUND_STATION_INDEX).getId(),
                        insertedStationEntities.get(UPBOUND_STATION_INDEX).getName()),
                new Station(insertedStationEntities.get(DOWNBOUND_STATION_INDEX).getId(),
                        insertedStationEntities.get(DOWNBOUND_STATION_INDEX).getName()));
    }

    public Station saveStation(final Station insertStation, final Long lineId) {
        StationEntity insertedStation = stationDao.insert(new StationEntity(insertStation.getName(), lineId));
        return new Station(insertedStation.getId(), insertedStation.getName());
    }

    public void updateBoundSection(Long lineId, Station baseStation, Station insertStation, String direction, int distance) {
        if (direction.equals("left")) {
            sectionDao.insert(new SectionEntity(insertStation.getId(), baseStation.getId(), lineId, distance));
            return;
        }
        sectionDao.insert(new SectionEntity(baseStation.getId(), insertStation.getId(), lineId, distance));
    }

    public void updateInterSection(final Long lineId, final Section deleteSection, final List<Section> sections) {
        sectionDao.deleteById(deleteSection.getId());
        List<SectionEntity> sectionEntities = sections.stream()
                .map(section -> new SectionEntity(section.getLeftStation().getId(), section.getRightStation().getId(), lineId, section.getDistance()))
                .collect(Collectors.toList());
        sectionDao.insertBoth(sectionEntities);
    }

    public Station findStationById(Long stationId) {
        StationEntity stationEntity = stationDao.findById(stationId).orElseThrow(NotFoundStationException::new);
        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    public void deleteSectionAndAllStation(final Section section) {
        sectionDao.deleteById(section.getId());
        System.out.println(section.getLeftStation().getId()+ " " + section.getRightStation().getId());
        stationDao.deleteBothById(List.of(
                section.getLeftStation().getId(),
                section.getRightStation().getId()));
    }

    public void deleteSectionAndStation(Section section, Station station) {
        sectionDao.deleteById(section.getId());
        stationDao.deleteById(station.getId());
    }

    public void updateSectionAndDeleteStation(final Long lineId, final List<Section> sectionsWithStation, final Section section, final Station station) {
        sectionDao.deleteBothById(sectionsWithStation.stream()
                .map(Section::getId)
                .collect(Collectors.toList()));


        sectionDao.insert(new SectionEntity(
                section.getLeftStation().getId(),
                section.getRightStation().getId(),
                lineId,
                section.getDistance()
        ));

        stationDao.deleteById(station.getId());
    }

    public Station findStationByName(final String station, final String lineName) {
        StationEntity stationEntity = stationDao.findByName(station, lineName).orElseThrow(NotFoundStationException::new);
        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    public Sections readAllSection() {
        List<SectionStationEntity> sectionStationEntities = sectionDao.findAll();
        return new Sections(getSections(sectionStationEntities));
    }
}
