package subway.application.mapper;

import static subway.exception.ErrorCode.STATION_NOT_FOUND;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.line.LineWithSectionRes;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.StationName;
import subway.exception.NotFoundException;

public final class SectionProvider {

    public static Sections createSections(final List<LineWithSectionRes> lineWithSections) {
        final List<Section> sections = lineWithSections.stream().map(res -> {
            final Station sourceStation = new Station(res.getSourceStationName());
            final Station targetStation = new Station(res.getTargetStationName());
            return new Section(sourceStation, targetStation, res.getDistance());
        }).collect(Collectors.toList());
        return new Sections(sections);
    }

    public static Long getStationIdByName(final StationName stationName,
                                          final List<LineWithSectionRes> lineWithSections) {
        return lineWithSections.stream()
            .filter(res -> res.isSourceOrTargetStation(stationName))
            .findFirst()
            .map(res -> res.getStationIdByStationName(stationName))
            .orElseThrow(() -> new NotFoundException(STATION_NOT_FOUND.getMessage() + " name = " + stationName));
    }
}
