package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.LineEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.Stations;
import subway.service.dto.request.StationRegisterRequest;

@Service
public class StationService {

    private final SectionService sectionService;

    private final LineMakerService lineMakerService;
    private final LineService lineService;

    public StationService(
            final SectionService sectionService,
            final LineMakerService lineMakerService,
            final LineService lineService
    ) {
        this.sectionService = sectionService;
        this.lineMakerService = lineMakerService;
        this.lineService = lineService;
    }

    public void registerStation(final StationRegisterRequest stationRegisterRequest) {

        final String lineName = stationRegisterRequest.getLineName();

        final Line line = lineMakerService.mapToLineFrom(lineName);
        line.add(mapToSectionFrom(stationRegisterRequest));

        sectionService.updateLine(lineMakerService.getLineEntityByName(lineName), line);
    }

    private Section mapToSectionFrom(final StationRegisterRequest stationRegisterRequest) {
        final Stations newStations = new Stations(
                new Station(stationRegisterRequest.getCurrentStationName()),
                new Station(stationRegisterRequest.getNextStationName()),
                stationRegisterRequest.getDistance()
        );

        return new Section(newStations);
    }

    public void deleteTargetStationInLine(final long lineId, final String station) {

        final LineEntity lineEntity = lineMakerService.getLineEntityById(lineId);
        final Line line = lineMakerService.mapToLineFrom(lineEntity.getName());

        line.deleteTargetStation(new Station(station));

        if (line.isDeleted()) {
            sectionService.deleteAll(lineEntity.getId());
            lineService.deleteLine(lineEntity.getId());
            return;
        }

        sectionService.updateLine(lineEntity, line);
    }
}
