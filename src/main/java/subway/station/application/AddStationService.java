package subway.station.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.output.FindAllLinePort;
import subway.line.application.port.output.GetLineByIdPort;
import subway.line.domain.Line;
import subway.line.domain.Subway;
import subway.section.application.port.output.DeleteSectionByLineIdPort;
import subway.section.application.port.output.SaveAllSectionPort;
import subway.section.domain.Sections;
import subway.station.application.port.input.AddStationUseCase;
import subway.station.application.port.output.SaveStationPort;
import subway.station.domain.Station;
import subway.station.dto.StationSaveRequest;

@RequiredArgsConstructor
@Transactional
@Service
public class AddStationService implements AddStationUseCase {
    private final FindAllLinePort findAllLinePort;
    private final GetLineByIdPort getLineByIdPort;
    private final DeleteSectionByLineIdPort deleteSectionByLineIdPort;
    private final SaveAllSectionPort saveAllSectionPort;
    private final SaveStationPort saveStationPort;
    
    @Override
    public Long addStation(final StationSaveRequest request) {
        final Subway subway = new Subway(findAllLinePort.findAll());
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
