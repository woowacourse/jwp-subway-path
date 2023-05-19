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
import subway.station.application.port.input.DeleteStationOnTheLineUseCase;
import subway.station.application.port.output.GetStationByIdPort;
import subway.station.domain.Station;

@Transactional
@Service
public class DeleteStationOnTheLineService implements DeleteStationOnTheLineUseCase {
    private final GetAllLinePort getAllLinePort;
    private final GetLineByIdPort getLineByIdPort;
    private final GetStationByIdPort getStationByIdPort;
    private final DeleteSectionByLineIdPort deleteSectionByLineIdPort;
    private final SaveAllSectionPort saveAllSectionPort;
    
    public DeleteStationOnTheLineService(
            final GetAllLinePort getAllLinePort,
            final GetLineByIdPort getLineByIdPort,
            final GetStationByIdPort getStationByIdPort,
            final DeleteSectionByLineIdPort deleteSectionByLineIdPort,
            final SaveAllSectionPort saveAllSectionPort
    ) {
        this.getAllLinePort = getAllLinePort;
        this.getLineByIdPort = getLineByIdPort;
        this.getStationByIdPort = getStationByIdPort;
        this.deleteSectionByLineIdPort = deleteSectionByLineIdPort;
        this.saveAllSectionPort = saveAllSectionPort;
    }
    
    @Override
    public void deleteStationOnTheLine(final Long lineId, final Long stationId) {
        final Subway subway = new Subway(getAllLinePort.getAll());
        final Line lineById = getLineByIdPort.getLineById(lineId);
        final Station stationById = getStationByIdPort.getStationById(stationId);
        final Line modifiedLine = subway.removeStation(lineById.getName(), stationById.getName());
        
        deleteSectionByLineIdPort.deleteSectionByLineId(lineId);
        final Sections sections = modifiedLine.getSections();
        saveAllSectionPort.saveAll(sections.getSections(), lineId);
    }
}
