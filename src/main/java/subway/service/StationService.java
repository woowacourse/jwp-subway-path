package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.LineEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.Stations;
import subway.service.dto.StationDeleteRequest;
import subway.service.dto.StationRegisterRequest;

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

    public void deleteStation(final StationDeleteRequest stationDeleteRequest) {

        final String lineName = stationDeleteRequest.getLineName();
        final LineEntity lineEntity = lineMakerService.getLineEntityByName(lineName);
        final Line line = lineMakerService.mapToLineFrom(lineName);

        line.delete(new Station(stationDeleteRequest.getStationName()));

        if (line.isDeleted()) {
            sectionService.deleteAll(lineEntity.getId());
            lineService.deleteLine(lineEntity.getId());
            return;
        }

        sectionService.updateLine(lineEntity, line);
    }
}
