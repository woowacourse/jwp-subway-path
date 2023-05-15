package subway.station.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.output.GetAllLinePort;
import subway.line.application.port.output.GetLineByIdPort;
import subway.line.domain.Line;
import subway.line.domain.Subway;
import subway.section.application.port.output.DeleteSectionByLineIdPort;
import subway.section.application.port.output.SaveAllSectionPort;
import subway.section.domain.Sections;
import subway.station.application.port.input.DeleteStationUseCase;
import subway.station.application.port.output.GetStationByIdPort;
import subway.station.domain.Station;

@RequiredArgsConstructor
@Transactional
@Service
public class DeleteStationService implements DeleteStationUseCase {
    private final GetAllLinePort getAllLinePort;
    private final GetLineByIdPort getLineByIdPort;
    private final GetStationByIdPort getStationByIdPort;
    private final DeleteSectionByLineIdPort deleteSectionByLineIdPort;
    private final SaveAllSectionPort saveAllSectionPort;
    
    @Override
    public void deleteStation(final Long lineId, final Long stationId) {
        final Subway subway = new Subway(getAllLinePort.findAll());
        final Line lineById = getLineByIdPort.getLineById(lineId);
        final Station stationById = getStationByIdPort.getStationById(stationId);
        final Line modifiedLine = subway.removeStation(lineById.getName(), stationById.getName());
        
        deleteSectionByLineIdPort.deleteSectionByLineId(lineId);
        final Sections sections = modifiedLine.getSections();
        saveAllSectionPort.saveAll(sections.getSections(), lineId);
    }
}
