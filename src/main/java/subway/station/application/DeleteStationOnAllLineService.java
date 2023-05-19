package subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.output.GetAllLinePort;
import subway.line.domain.Line;
import subway.line.domain.Subway;
import subway.section.application.port.output.DeleteSectionByLinesPort;
import subway.section.application.port.output.SaveAllSectionsOfLinesPort;
import subway.station.application.port.input.DeleteStationOnAllLineUseCase;
import subway.station.application.port.output.GetStationByIdPort;
import subway.station.domain.Station;

import java.util.Set;

@Transactional
@Service
public class DeleteStationOnAllLineService implements DeleteStationOnAllLineUseCase {
    private final GetAllLinePort getAllLinePort;
    private final GetStationByIdPort getStationByIdPort;
    private final DeleteSectionByLinesPort deleteSectionByLinesPort;
    private final SaveAllSectionsOfLinesPort saveAllSectionsOfLinesPort;
    
    public DeleteStationOnAllLineService(
            final GetAllLinePort getAllLinePort,
            final GetStationByIdPort getStationByIdPort,
            final DeleteSectionByLinesPort deleteSectionByLinesPort,
            final SaveAllSectionsOfLinesPort saveAllSectionsOfLinesPort
    ) {
        this.getAllLinePort = getAllLinePort;
        this.getStationByIdPort = getStationByIdPort;
        this.deleteSectionByLinesPort = deleteSectionByLinesPort;
        this.saveAllSectionsOfLinesPort = saveAllSectionsOfLinesPort;
    }
    
    @Override
    public void deleteStationOnAllLine(final Long stationId) {
        final Subway subway = new Subway(getAllLinePort.getAll());
        final Station station = getStationByIdPort.getStationById(stationId);
        final Set<Line> modifiedLines = subway.removeStationOnAllLine(station.getName());
        
        deleteSectionByLinesPort.deleteSectionByLines(modifiedLines);
        saveAllSectionsOfLinesPort.saveAllSectionsOfLines(modifiedLines);
    }
}
