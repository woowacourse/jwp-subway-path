package subway.repository;

import org.springframework.stereotype.Component;
import subway.dao.LineEntity;
import subway.dao.SectionEntity;
import subway.dao.StationEntity;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;

import java.util.List;

@Component
public class Mapper {

    public LineEntity toLineEntity(final Line line) {
        return new LineEntity(line.getId(), line.getNameValue(), line.getColorValue());
    }

    public Line toLine(final LineEntity lineEntity, final List<Section> sections){
        return new Line(
                lineEntity.getId(),
                lineEntity.getName(),
                lineEntity.getColor(),
                sections);
    }

    public Section toSection(final SectionEntity sectionEntity, final StationEntity from, final StationEntity to) {
        return new Section(
                sectionEntity.getId(),
                toStation(from),
                toStation(to),
                sectionEntity.getDistance()
        );
    }

    public SectionEntity toSectionEntity(final Section section, final Long lineId) {
        return new SectionEntity(section.getId(), section.getFromId(), section.getToId(), section.getDistanceValue(), lineId);
    }

    public StationEntity toStationEntity(final Station station) {
        return new StationEntity(station.getId(), station.getName());
    }

    public Station toStation(final StationEntity stationEntity) {
        return new Station(stationEntity.getId(), stationEntity.getName());
    }
}
