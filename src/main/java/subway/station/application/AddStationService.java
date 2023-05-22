package subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.output.GetAllLinePort;
import subway.line.application.port.output.GetLineByIdPort;
import subway.line.domain.Line;
import subway.line.domain.Subway;
import subway.section.application.port.output.DeleteSectionByLineIdPort;
import subway.section.application.port.output.SaveAllSectionPort;
import subway.section.domain.Sections;
import subway.station.application.port.input.AddStationUseCase;
import subway.station.application.port.output.SaveStationPort;
import subway.station.domain.Station;
import subway.station.dto.AddStationRequest;

@Transactional
@Service
public class AddStationService implements AddStationUseCase {
    private final GetAllLinePort getAllLinePort;
    private final GetLineByIdPort getLineByIdPort;
    private final DeleteSectionByLineIdPort deleteSectionByLineIdPort;
    private final SaveStationPort saveStationPort;
    private final SaveAllSectionPort saveAllSectionPort;
    
    public AddStationService(
            final GetAllLinePort getAllLinePort,
            final GetLineByIdPort getLineByIdPort,
            final DeleteSectionByLineIdPort deleteSectionByLineIdPort,
            final SaveStationPort saveStationPort,
            final SaveAllSectionPort saveAllSectionPort
    ) {
        this.getAllLinePort = getAllLinePort;
        this.getLineByIdPort = getLineByIdPort;
        this.deleteSectionByLineIdPort = deleteSectionByLineIdPort;
        this.saveStationPort = saveStationPort;
        this.saveAllSectionPort = saveAllSectionPort;
    }
    
    @Override
    public Long addStation(final AddStationRequest request) {
        final Subway subway = new Subway(getAllLinePort.getAll());
        final Line lineById = getLineByIdPort.getLineById(request.getLineId());
        final Line modifiedLine = subway.addStation(
                lineById.getName(),
                request.getBaseStation(),
                request.getDirection(),
                request.getAdditionalStation(),
                request.getDistance()
        );
        
        deleteSectionByLineIdPort.deleteSectionByLineId(request.getLineId());
        final Long stationId = saveStationPort.saveStation(new Station(request.getAdditionalStation()));
        final Sections sections = modifiedLine.getSections();
        saveAllSectionPort.saveAll(sections.getSections(), request.getLineId());
        return stationId;
    }
}
