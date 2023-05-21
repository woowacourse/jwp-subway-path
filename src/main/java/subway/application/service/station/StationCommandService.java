package subway.application.service.station;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.port.in.section.RemoveStationFromLineUseCase;
import subway.application.port.in.section.dto.command.RemoveStationFromLineCommand;
import subway.application.port.in.station.CreateStationUseCase;
import subway.application.port.in.station.DeleteStationUseCase;
import subway.application.port.in.station.UpdateStationUseCase;
import subway.application.port.in.station.dto.command.CreateStationCommand;
import subway.application.port.in.station.dto.command.UpdateStationCommand;
import subway.application.port.out.line.LoadLinePort;
import subway.application.port.out.station.LoadStationPort;
import subway.application.port.out.station.PersistStationPort;
import subway.common.exception.NoSuchStationException;
import subway.common.exception.SubwayIllegalArgumentException;
import subway.domain.Station;

@Service
@Transactional
public class StationCommandService implements CreateStationUseCase, UpdateStationUseCase, DeleteStationUseCase {

    private final LoadStationPort loadStationPort;
    private final PersistStationPort persistStationPort;
    private final LoadLinePort loadLinePort;
    private final RemoveStationFromLineUseCase removeStationFromLineUseCase;

    public StationCommandService(final LoadStationPort loadStationPort,
            final PersistStationPort persistStationPort,
            final LoadLinePort loadLinePort,
            final RemoveStationFromLineUseCase removeStationFromLineUseCase) {
        this.loadStationPort = loadStationPort;
        this.persistStationPort = persistStationPort;
        this.loadLinePort = loadLinePort;
        this.removeStationFromLineUseCase = removeStationFromLineUseCase;
    }

    @Override
    public long createStation(final CreateStationCommand command) {
        loadStationPort.findByName(command.getName())
                .ifPresent(it -> {
                    throw new SubwayIllegalArgumentException("기존 역과 중복된 이름입니다.");
                });
        return persistStationPort.create(new Station(command.getName()));
    }


    @Override
    public void updateStation(final UpdateStationCommand command) {
        loadStationPort.findById(command.getStationId())
                .orElseThrow(NoSuchStationException::new);

        persistStationPort.update(new Station(command.getStationId(), command.getName()));
    }

    @Override
    public void deleteStation(final long stationId) {
        Station station = loadStationPort.findById(stationId)
                .orElseThrow(NoSuchStationException::new);

        removeStationFromLine(stationId, station);

        persistStationPort.deleteById(stationId);
    }

    private void removeStationFromLine(final long stationId, final Station station) {
        List<Long> containingLineIds = loadLinePort.findContainingLineIdsByStation(station);

        for (final Long lineId : containingLineIds) {
            RemoveStationFromLineCommand command = new RemoveStationFromLineCommand(lineId, stationId);
            removeStationFromLineUseCase.removeStation(command);
        }
    }
}
