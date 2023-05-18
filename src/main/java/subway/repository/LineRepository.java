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
import subway.exception.line.AlreadyExistLineException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class LineRepository {

    private static final int UPBOUND_STATION_INDEX = 0;
    private static final int DOWNBOUND_STATION_INDEX = 1;
    private static final int LAST_SECTION_INDEX = 0;

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineRepository(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Line save(final Line line) {
        Optional<LineEntity> findedLine = lineDao.findByName(line.getName());
        if (findedLine.isPresent()) {
            throw new AlreadyExistLineException();
        }
        LineEntity insertedLineEntity = lineDao.insert(new LineEntity(line.getName(), line.getColor()));
        return new Line(
                insertedLineEntity.getId(),
                new Name(insertedLineEntity.getName()),
                new Color(insertedLineEntity.getColor()),
                new Sections(List.of())
        );
    }

    public Line findByName(final String name) {
        LineEntity lineEntity = lineDao.findByName(name)
                .orElseThrow(NotFoundLineException::new);

        List<SectionStationEntity> sectionStationEntities = sectionDao.findByLineId(lineEntity.getId());
        if (sectionStationEntities.isEmpty()) {
            return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
        }

        List<Section> sections = getSections(sectionStationEntities);
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), sections);
    }

    public Line findByStationId(final Long id) {
        LineEntity lineEntity = lineDao.findByStationId(id).orElseThrow(NotFoundLineException::new);
        List<SectionStationEntity> sectionStationEntities = sectionDao.findByLineId(lineEntity.getId());

        List<Section> sections = getSections(sectionStationEntities);
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), sections);
    }

    public Line findById(final Long id) {
        LineEntity lineEntity = lineDao.findById(id)
                .orElseThrow(NotFoundLineException::new);
        List<SectionStationEntity> sectionStationEntities = sectionDao.findByLineId(lineEntity.getId());

        List<Section> sections = getSections(sectionStationEntities);
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), sections);
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

    private List<Section> getSections(List<SectionStationEntity> sectionStationEntities) {
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

    public Station findStationById(Long stationId) {
        StationEntity stationEntity = stationDao.findById(stationId).orElseThrow(NotFoundStationException::new);
        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    public Station findStationByName(final String station, final String lineName) {
        StationEntity stationEntity = stationDao.findByName(station, lineName).orElseThrow(NotFoundStationException::new);
        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    public Sections readAllSection() {
        List<SectionStationEntity> sectionStationEntities = sectionDao.findAll();
        return new Sections(getSections(sectionStationEntities));
    }

    public void updateSectionAndDeleteStation(final Long lineId, final Sections updateSections, final Station station) {
        List<Section> existSections = getSections(sectionDao.findByLineId(lineId));
        Sections sections = new Sections(existSections);
        if (updateSections.isEmpty()) {
            List<Long> stationIds = List.of(sections.findUpBoundStation().getId(), sections.findDownBoundStation().getId());
            stationDao.deleteBothById(stationIds);
            sectionDao.deleteById(existSections.get(LAST_SECTION_INDEX).getId());
            return;
        }
        deleteUpdateSection(updateSections, lineId);
        addUpdateSection(updateSections, lineId);
        stationDao.deleteById(station.getId());
    }

    public void updateSection(final Sections sections, final Long lineId) {
        deleteUpdateSection(sections, lineId);
        addUpdateSection(sections, lineId);
    }

    private void deleteUpdateSection(final Sections sections, final Long lineId) {
        List<Section> updatedSections = sections.getSections();
        List<Section> existedSections = getSections(sectionDao.findByLineId(lineId));
        existedSections.removeAll(updatedSections);

        List<Long> sectiondIds = existedSections.stream()
                .map(Section::getId).
                collect(Collectors.toList());

        if (sectiondIds.isEmpty()) {
            return;
        }
        sectionDao.deleteBothById(sectiondIds);
    }

    private void addUpdateSection(final Sections sections, final Long lineId) {
        List<Section> updatedSections = sections.getSections();
        List<Section> existedSections = getSections(sectionDao.findByLineId(lineId));
        updatedSections.removeAll(existedSections);

        sectionDao.insertBoth(updatedSections.stream()
                .map(section -> new SectionEntity(
                        section.getLeftStation().getId(),
                        section.getRightStation().getId(),
                        lineId, section.getDistance())).
                collect(Collectors.toList()));
    }
}
