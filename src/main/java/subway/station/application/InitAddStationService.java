package subway.station.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.output.FindAllLinePort;
import subway.line.application.port.output.GetLineByIdPort;
import subway.line.domain.Line;
import subway.line.domain.Subway;
import subway.section.application.port.output.SaveSectionPort;
import subway.station.application.port.input.InitAddStationUseCase;
import subway.station.application.port.output.SaveAllStationPort;
import subway.station.dto.StationInitSaveRequest;

@RequiredArgsConstructor
@Transactional
@Service
public class InitAddStationService implements InitAddStationUseCase {
    private final SaveAllStationPort saveAllStationPort;
    private final FindAllLinePort findAllLinePort;
    private final GetLineByIdPort getLineByIdPort;
    private final SaveSectionPort saveSectionPort;
    
    @Override
    public void initAddStations(final StationInitSaveRequest request) {
        final Subway subway = new Subway(findAllLinePort.findAll());
        
        final Line line = getLineByIdPort.getLineById(request.getLineId());
        subway.initAddStation(line.getName(), request.getFirstStation(), request.getSecondStation(), request.getDistance());
        
        saveAllStationPort.saveAll(request.toEntities());
        saveSectionPort.save(request.toSectionEntity(), request.getLineId());
    }
}
